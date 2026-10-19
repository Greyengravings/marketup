import { Router } from 'express';
import { v4 as uuidv4 } from 'uuid';
import { db } from '../db.js';
import { requireAuth } from '../middleware/auth.js';
import { contentGenerationService } from '../services/contentGenerationService.js';

export const aiContentRouter = Router();

function serializePost(row) {
  return {
    id: row.id,
    business_id: row.business_id,
    occasion: row.occasion,
    caption: row.caption,
    hashtags: JSON.parse(row.hashtags || '[]'),
    cta: row.cta,
    image_prompt: row.image_prompt,
    generated_image: row.generated_image,
    reel_idea: row.reel_idea,
    carousel_slides: JSON.parse(row.carousel_slides || '[]'),
    status: row.status,
    ai_model_used: row.ai_model_used,
    created_at: row.created_at,
  };
}

function getBusinessOr404(req, res) {
  const business = db
    .prepare('SELECT * FROM businesses WHERE user_id = ?')
    .get(req.user.id);
  if (!business) {
    res.status(404).json({ detail: 'Complete business onboarding first' });
    return null;
  }
  return business;
}

aiContentRouter.post('/ai/generate', requireAuth, async (req, res) => {
  const business = getBusinessOr404(req, res);
  if (!business) return;

  const options = req.body || {};
  const occasion = options.occasion || null;

  let result;
  try {
    // Determine mode from config or query param if we want flexibility
    // For now, it defaults to 'preset' as per service implementation
    result = await contentGenerationService.generatePostContent(business, options);
  } catch (err) {
    if (err.message === 'AI_NOT_CONFIGURED') {
      return res.status(503).json({ detail: 'AI generation not configured' });
    }
    return res.status(502).json({ detail: `Content generation failed: ${err.message}` });
  }

  const id = uuidv4();
  db.prepare(
    `INSERT INTO posts
      (id, business_id, occasion, caption, hashtags, cta, image_prompt,
       generated_image, reel_idea, carousel_slides, status, ai_model_used)
     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'pending', ?)`,
  ).run(
    id,
    business.id,
    occasion,
    result.caption,
    JSON.stringify(result.hashtags),
    result.cta,
    result.image_prompt,
    result.generated_image || null,
    result.reel_idea || null,
    JSON.stringify(result.carousel_slides || []),
    result.model_used,
  );

  const row = db.prepare('SELECT * FROM posts WHERE id = ?').get(id);
  return res.status(201).json(serializePost(row));
});

aiContentRouter.get('/businesses/me/posts', requireAuth, (req, res) => {
  const business = getBusinessOr404(req, res);
  if (!business) return;

  const statusFilter = req.query.status_filter;
  const rows = statusFilter
    ? db
        .prepare(
          'SELECT * FROM posts WHERE business_id = ? AND status = ? ORDER BY created_at DESC',
        )
        .all(business.id, statusFilter)
    : db
        .prepare('SELECT * FROM posts WHERE business_id = ? ORDER BY created_at DESC')
        .all(business.id);

  return res.json(rows.map(serializePost));
});

aiContentRouter.post('/posts/:id/approve', requireAuth, (req, res) => {
  const business = getBusinessOr404(req, res);
  if (!business) return;

  const post = db
    .prepare('SELECT * FROM posts WHERE id = ? AND business_id = ?')
    .get(req.params.id, business.id);
  if (!post) return res.status(404).json({ detail: 'Post not found' });

  db.prepare(
    "UPDATE posts SET status = 'scheduled', approved_at = datetime('now') WHERE id = ?",
  ).run(post.id);

  return res.json({ id: post.id, status: 'scheduled' });
});

aiContentRouter.post('/posts/:id/reject', requireAuth, (req, res) => {
  const business = getBusinessOr404(req, res);
  if (!business) return;

  const post = db
    .prepare('SELECT * FROM posts WHERE id = ? AND business_id = ?')
    .get(req.params.id, business.id);
  if (!post) return res.status(404).json({ detail: 'Post not found' });

  db.prepare("UPDATE posts SET status = 'rejected' WHERE id = ?").run(post.id);

  return res.json({ id: post.id, status: 'rejected' });
});
