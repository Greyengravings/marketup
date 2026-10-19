import { Router } from 'express';
import { v4 as uuidv4 } from 'uuid';
import { db } from '../db.js';
import { requireAuth } from '../middleware/auth.js';

export const businessRouter = Router();

function serializeBusiness(row) {
  return {
    id: row.id,
    business_name: row.business_name,
    owner_name: row.owner_name,
    sector: row.sector,
    sub_sector: row.sub_sector,
    description: row.description,
    address: row.address,
    city: row.city,
    phone: row.phone,
    whatsapp: row.whatsapp,
    email: row.email,
    brand_colors: JSON.parse(row.brand_colors || '[]'),
    brand_personality: row.brand_personality,
    tone_of_voice: row.tone_of_voice,
    target_audience: row.target_audience,
    preferred_cta: row.preferred_cta,
    social_handles: JSON.parse(row.social_handles || '{}'),
    logo_url: row.logo_url,
    website: row.website,
    created_at: row.created_at,
  };
}

businessRouter.post('/', requireAuth, (req, res) => {
  // Allow re-onboarding by deleting existing profile if it exists
  db.prepare('DELETE FROM businesses WHERE user_id = ?').run(req.user.id);

  const body = req.body || {};
  if (!body.business_name || !body.sector) {
    return res.status(422).json({ detail: 'business_name and sector are required' });
  }

  const id = uuidv4();
  db.prepare(
    `INSERT INTO businesses
      (id, user_id, business_name, owner_name, sector, sub_sector, description,
       address, city, phone, whatsapp, email, brand_colors, brand_personality,
       tone_of_voice, target_audience, preferred_cta, social_handles, website)
     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
  ).run(
    id,
    req.user.id,
    body.business_name,
    body.owner_name || null,
    body.sector,
    body.sub_sector || null,
    body.description || null,
    body.address || null,
    body.city || null,
    body.phone || null,
    body.whatsapp || null,
    body.email || null,
    JSON.stringify(body.brand_colors || []),
    body.brand_personality || null,
    body.tone_of_voice || null,
    body.target_audience || null,
    body.preferred_cta || null,
    JSON.stringify(body.social_handles || {}),
    body.website || null,
  );

  const row = db.prepare('SELECT * FROM businesses WHERE id = ?').get(id);
  return res.status(201).json(serializeBusiness(row));
});

businessRouter.get('/me', requireAuth, (req, res) => {
  const row = db.prepare('SELECT * FROM businesses WHERE user_id = ?').get(req.user.id);
  if (!row) {
    return res
      .status(404)
      .json({ detail: 'No business profile found — complete onboarding first' });
  }
  return res.json(serializeBusiness(row));
});

businessRouter.patch('/me', requireAuth, (req, res) => {
  const row = db.prepare('SELECT * FROM businesses WHERE user_id = ?').get(req.user.id);
  if (!row) {
    return res.status(404).json({ detail: 'No business profile found' });
  }

  const body = req.body || {};
  db.prepare(
    `UPDATE businesses SET
      business_name = ?, owner_name = ?, sector = ?, sub_sector = ?, description = ?,
      address = ?, city = ?, phone = ?, whatsapp = ?, email = ?, brand_colors = ?,
      brand_personality = ?, tone_of_voice = ?, target_audience = ?,
      preferred_cta = ?, social_handles = ?, website = ?
     WHERE id = ?`,
  ).run(
    body.business_name ?? row.business_name,
    body.owner_name ?? row.owner_name,
    body.sector ?? row.sector,
    body.sub_sector ?? row.sub_sector,
    body.description ?? row.description,
    body.address ?? row.address,
    body.city ?? row.city,
    body.phone ?? row.phone,
    body.whatsapp ?? row.whatsapp,
    body.email ?? row.email,
    JSON.stringify(body.brand_colors ?? JSON.parse(row.brand_colors || '[]')),
    body.brand_personality ?? row.brand_personality,
    body.tone_of_voice ?? row.tone_of_voice,
    body.target_audience ?? row.target_audience,
    body.preferred_cta ?? row.preferred_cta,
    JSON.stringify(body.social_handles ?? JSON.parse(row.social_handles || '{}')),
    body.website ?? row.website,
    row.id,
  );

  const updated = db.prepare('SELECT * FROM businesses WHERE id = ?').get(row.id);
  return res.json(serializeBusiness(updated));
});

export { serializeBusiness };
