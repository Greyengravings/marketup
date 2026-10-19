import Anthropic from '@anthropic-ai/sdk';
import { config } from '../config.js';

const SYSTEM_PROMPT = `You are MarketUp's AI social media copywriter for small Indian businesses.
You write concise, warm, locally-flavoured social media posts that match a business's brand voice.
You ALWAYS respond with ONLY a single valid JSON object, no markdown fences, no preamble, matching exactly:
{
  "caption": "string, 2-4 sentences, engaging, no hashtags inside it",
  "hashtags": ["#tag1", "#tag2", "... 5 to 8 relevant hashtags"],
  "cta": "short call to action, e.g. 'Visit us today!'",
  "image_prompt": "a descriptive prompt for generating a branded social media image for this post"
}`;

function buildUserPrompt(business, occasion) {
  const today = new Date().toISOString().slice(0, 10);
  const occasionLine = occasion
    ? `Occasion / theme to write about: ${occasion}.`
    : `No specific occasion was given — pick something naturally relevant for ` +
      `today (${today}) for this type of business (e.g. a seasonal angle, a ` +
      `general trust/quality angle, or a light promotional angle).`;

  const brandColors = business.brand_colors ? JSON.parse(business.brand_colors) : [];
  const colors = brandColors.length ? brandColors.join(', ') : 'not specified';

  return `Business name: ${business.business_name}
Sector: ${business.sector} / ${business.sub_sector || 'n/a'}
Description: ${business.description || 'n/a'}
Location/address: ${business.address || 'n/a'}
Brand colors: ${colors}
${occasionLine}

Write one social media post for this business following the JSON schema exactly.`;
}

export async function generatePostContent(business, occasion) {
  if (!config.anthropicApiKey) {
    const err = new Error(
      'ANTHROPIC_API_KEY is not set. Add it to backend-js/.env to enable live AI generation.',
    );
    err.code = 'AI_NOT_CONFIGURED';
    throw err;
  }

  const client = new Anthropic({ apiKey: config.anthropicApiKey });

  const message = await client.messages.create({
    model: config.anthropicModel,
    max_tokens: 600,
    system: SYSTEM_PROMPT,
    messages: [{ role: 'user', content: buildUserPrompt(business, occasion) }],
  });

  let rawText = message.content
    .filter((block) => block.type === 'text')
    .map((block) => block.text)
    .join('')
    .trim();

  if (rawText.startsWith('```')) {
    rawText = rawText.replace(/^```(json)?/, '').replace(/```$/, '').trim();
  }

  const data = JSON.parse(rawText);

  return {
    caption: data.caption,
    hashtags: data.hashtags,
    cta: data.cta,
    image_prompt: data.image_prompt,
    model_used: config.anthropicModel,
  };
}
