import Anthropic from '@anthropic-ai/sdk';
import { config } from '../config.js';

const PRESET_TEMPLATES = {
  'Restaurant': {
    'Daily Post': [
      "Looking for the best {subSector} in {city}? At {businessName}, we serve delicious meals made with fresh ingredients. Visit us today! {cta}",
      "Hungry? We've got you covered at {businessName}. Experience authentic {subSector} right here in {city}. {cta}"
    ],
    'Promotional Post': [
      "Delicious food, better prices! Join us at {businessName} this week for special deals on our entire {subSector} menu. {cta}",
      "Treat yourself without breaking the bank. {businessName} in {city} is now offering exclusive discounts. {cta}"
    ],
    'Festival Wishes': [
      "Celebrate the joy of festivities with great food at {businessName}! Wishing you a wonderful time ahead. {cta}"
    ],
    'Product Highlight': [
      "Our signature {subSector} is a must-try! Made with traditional recipes at {businessName}. {cta}"
    ]
  },
  'Bakery': {
    'Daily Post': [
      "Freshly baked {subSector} waiting for you at {businessName}! Start your day with sweetness in {city}. {cta}",
      "Nothing beats the smell of fresh bread. Visit {businessName} today! {cta}"
    ],
    'Promotional Post': [
      "Special offer on all cakes this weekend at {businessName}! Order your favorites now. {cta}"
    ]
  },
  'Salon': {
    'Daily Post': [
      "Time for a makeover? Visit {businessName} in {city} for premium {subSector} services. {cta}",
      "Look your best, feel your best. Our experts at {businessName} are here to help. {cta}"
    ],
    'Service Highlight': [
      "Experience our rejuvenating {subSector} treatment at {businessName}. Book your appointment now! {cta}"
    ]
  },
  'Gym': {
    'Daily Post': [
      "Ready to transform your life? Join {businessName} in {city} today! Your journey to fitness starts here. {cta}",
      "Don't wait for tomorrow. Get fit now at {businessName}. Expert {subSector} coaching available. {cta}"
    ],
    'Tips': [
      "Stay hydrated and consistent! Consistency is key at {businessName}. #fitnessgoals"
    ]
  },
  'Clothing Store': {
    'Daily Post': [
      "Upgrade your wardrobe with the latest {subSector} trends at {businessName}, {city}. {cta}",
      "Find your perfect style at {businessName}. New arrivals every week! {cta}"
    ],
    'Promotional Post': [
      "End of season sale at {businessName}! Grab your favorite {subSector} at up to 50% off. {cta}"
    ]
  },
  'Real Estate': {
    'Daily Post': [
      "Finding your dream home in {city} just got easier with {businessName}. Specialized in {subSector}. {cta}",
      "Looking to invest in property? {businessName} offers the best deals in the region. {cta}"
    ],
    'Announcement': [
      "New project launch! Discover premium living spaces by {businessName} in {city}. {cta}"
    ]
  },
  'Education': {
    'Daily Post': [
      "Empowering students at {businessName} with quality {subSector} education in {city}. Join us today! {cta}",
      "Unlock your potential with expert coaching at {businessName}. Enroll for our next session. {cta}"
    ]
  },
  'Local Business': {
    'Daily Post': [
      "Proudly serving {city}! At {businessName}, we provide top-notch {subSector} services for {targetAudience}. {cta}",
      "Support local! Visit {businessName} for all your {subSector} needs. {cta}"
    ]
  },
  'Generic Business': {
    'Daily Post': [
      "At {businessName}, we are dedicated to providing the best {subSector} services in {city}. Contact us to learn more! {cta}",
      "Quality you can trust. {businessName} is your go-to for all things {subSector} in {city}. {cta}"
    ],
    'Brand Awareness': [
      "Did you know that {businessName} has been serving {city} for years? We specialize in premium {subSector} for {targetAudience}. {cta}"
    ],
    'Customer Review': [
      "See what our happy customers are saying about {businessName}! We pride ourselves on excellent {subSector}. {cta}"
    ]
  }
};

const DEFAULT_HASHTAGS = {
  'Restaurant': ['#foodie', '#instafood', '#restaurant', '#delicious', '#indianfood'],
  'Gym': ['#fitness', '#workout', '#gymlife', '#health', '#motivation'],
  'Generic': ['#business', '#quality', '#service', '#growth', '#localbusiness']
};

class PresetProvider {
  async generate(business, options = {}) {
    const { occasion, postType = 'Promotional Post', tone = 'Professional', goal = 'Brand Awareness' } = options;

    const sector = business.sector || 'Generic Business';
    const sectorTemplates = PRESET_TEMPLATES[sector] || PRESET_TEMPLATES['Generic Business'];
    const typeTemplates = sectorTemplates[postType] || sectorTemplates['Daily Post'] || PRESET_TEMPLATES['Generic Business']['Daily Post'];

    let template = typeTemplates[Math.floor(Math.random() * typeTemplates.length)];

    const caption = template
      .replace(/{businessName}/g, business.business_name || 'Our Business')
      .replace(/{city}/g, business.city || 'your city')
      .replace(/{subSector}/g, business.sub_sector || business.sector || 'services')
      .replace(/{targetAudience}/g, business.target_audience || 'everyone')
      .replace(/{cta}/g, business.preferred_cta || 'Visit us today!');

    const hashtags = DEFAULT_HASHTAGS[sector] || DEFAULT_HASHTAGS['Generic'];

    const imagePrompt = `A high quality ${tone.toLowerCase()} ${postType} image for ${business.business_name || 'Our Business'} in ${business.city || 'city'} highlighting ${occasion || business.sub_sector || 'services'}`;
    const seed = Math.floor(Math.random() * 1000000);
    const generatedImageUrl = `https://image.pollinations.ai/prompt/${encodeURIComponent(imagePrompt)}?width=800&height=800&nologo=true&seed=${seed}`;

    return {
      caption,
      hashtags,
      cta: business.preferred_cta || 'Contact us for more details!',
      image_prompt: imagePrompt,
      generated_image: generatedImageUrl,
      reel_idea: `A 15-second reel showing the behind-the-scenes of ${business.business_name}'s ${business.sub_sector || 'services'} process for ${occasion || 'our customers'}.`,
      carousel_slides: [
        `Welcome to ${business.business_name || 'Our Business'}`,
        `Our ${business.sub_sector || 'Services'} Highlights`,
        `Special Offer: ${occasion || 'Visit Us Today'}`
      ],
      model_used: 'PresetEngine-v1'
    };
  }
}

class OpenAIProvider {
  async generate(business, options = {}) {
    if (!config.openaiApiKey) {
      throw new Error('OPENAI_NOT_CONFIGURED');
    }

    const { occasion, tone = 'Professional', postType = 'Promotional Post', goal = 'Sales' } = options;

    const systemPrompt = `You are an expert AI social media copywriter. Write highly engaging, high-converting social media copy directly for the target audience.
CRITICAL INSTRUCTION: Do NOT include category titles, post type labels, goal names, or metadata tags (like "📢 Post Type:", "💡 Category:", "🎯 Goal:") inside the caption text itself. Write pure natural post copy with emojis and end with call to action.

You ALWAYS respond with ONLY a single valid JSON object, matching exactly:
{
  "caption": "string",
  "hashtags": ["string"],
  "cta": "string",
  "image_prompt": "string",
  "reel_idea": "string",
  "carousel_slides": ["string"]
}`;

    const userPrompt = `generate me a ${postType} with:
- Business Name: ${business.business_name || 'Our Business'}
- Owner Name: ${business.owner_name || 'Owner'}
- Sector / Category: ${business.sector || 'General'} (${business.sub_sector || 'General'})
- Description: ${business.description || 'Quality local business'}
- Location: ${business.city || business.address || 'Local City'}
- Brand Personality: ${business.brand_personality || 'Authentic, Welcoming & Professional'}
- Default Tone of Voice: ${business.tone_of_voice || 'Friendly & Professional'}
- Target Audience: ${business.target_audience || 'Local Customers'}
- Preferred CTA: ${business.preferred_cta || 'Visit us or contact us today!'}
- Social Handles: ${JSON.stringify(business.social_handles || {})}
- What Should We Create / Topic Input: ${occasion || 'General business update'}
- Desired Tone Override: ${tone}
- Campaign Goal: ${goal}
- Include Hashtags: ${options.includeHashtags !== false}
- Include CTA: ${options.includeCta !== false}
- Include Reel Idea: ${options.includeReelIdea === true}`;

    const response = await fetch('https://api.openai.com/v1/chat/completions', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${config.openaiApiKey}`
      },
      body: JSON.stringify({
        model: config.openaiModel,
        messages: [
          { role: 'system', content: systemPrompt },
          { role: 'user', content: userPrompt }
        ],
        temperature: 0.7,
        response_format: { type: 'json_object' }
      })
    });

    if (!response.ok) {
      const errBody = await response.text();
      throw new Error(`OpenAI API error (${response.status}): ${errBody}`);
    }

    const json = await response.json();
    const rawText = json.choices[0]?.message?.content?.trim() || '{}';
    const data = JSON.parse(rawText);

    const cleanImagePrompt = data.image_prompt || `${postType} for ${business.business_name} ${occasion}`;
    const seed = Math.floor(Math.random() * 1000000);
    const generatedImageUrl = `https://image.pollinations.ai/prompt/${encodeURIComponent(cleanImagePrompt)}?width=800&height=800&nologo=true&seed=${seed}`;

    return {
      ...data,
      generated_image: generatedImageUrl,
      model_used: config.openaiModel
    };
  }
}

class AnthropicProvider {
  async generate(business, options = {}) {
    if (!config.anthropicApiKey) {
      throw new Error('ANTHROPIC_NOT_CONFIGURED');
    }

    const client = new Anthropic({ apiKey: config.anthropicApiKey });
    const { occasion, tone = 'Professional', postType = 'Promotional Post', goal = 'Sales' } = options;

    const systemPrompt = `You are an expert AI social media copywriter. Write highly engaging social media copy.
CRITICAL INSTRUCTION: Do NOT include category titles, post type labels, goal names, or metadata tags inside the caption text itself. Write pure natural post copy.

You ALWAYS respond with ONLY a single valid JSON object, matching exactly:
{
  "caption": "string",
  "hashtags": ["string"],
  "cta": "string",
  "image_prompt": "string",
  "reel_idea": "string",
  "carousel_slides": ["string"]
}`;

    const userPrompt = `generate me a ${postType} with:
- Business Name: ${business.business_name || 'Our Business'}
- Owner Name: ${business.owner_name || 'Owner'}
- Sector / Category: ${business.sector || 'General'} (${business.sub_sector || 'General'})
- Description: ${business.description || 'Quality local business'}
- Location: ${business.city || business.address || 'Local City'}
- Brand Personality: ${business.brand_personality || 'Authentic, Welcoming & Professional'}
- Default Tone of Voice: ${business.tone_of_voice || 'Friendly & Professional'}
- Target Audience: ${business.target_audience || 'Local Customers'}
- Preferred CTA: ${business.preferred_cta || 'Visit us or contact us today!'}
- Social Handles: ${JSON.stringify(business.social_handles || {})}
- What Should We Create / Topic Input: ${occasion || 'General business update'}
- Desired Tone Override: ${tone}
- Campaign Goal: ${goal}
- Include Hashtags: ${options.includeHashtags !== false}
- Include CTA: ${options.includeCta !== false}
- Include Reel Idea: ${options.includeReelIdea === true}`;

    const message = await client.messages.create({
      model: config.anthropicModel,
      max_tokens: 1000,
      system: systemPrompt,
      messages: [{ role: 'user', content: userPrompt }],
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

    const cleanImagePrompt = data.image_prompt || `${postType} for ${business.business_name} ${occasion}`;
    const seed = Math.floor(Math.random() * 1000000);
    const generatedImageUrl = `https://image.pollinations.ai/prompt/${encodeURIComponent(cleanImagePrompt)}?width=800&height=800&nologo=true&seed=${seed}`;

    return {
      ...data,
      generated_image: generatedImageUrl,
      model_used: config.anthropicModel
    };
  }
}

class ContentGenerationService {
  constructor() {
    this.presetProvider = new PresetProvider();
    this.openAiProvider = new OpenAIProvider();
    this.anthropicProvider = new AnthropicProvider();
  }

  async generatePostContent(business, options = {}) {
    const hasOpenAiKey = Boolean(config.openaiApiKey && !config.openaiApiKey.includes('your-key-here'));
    const hasAnthropicKey = Boolean(config.anthropicApiKey && !config.anthropicApiKey.includes('your-key-here'));

    if (hasOpenAiKey) {
      try {
        return await this.openAiProvider.generate(business, options);
      } catch (err) {
        console.warn('OpenAI generation failed, falling back:', err.message);
      }
    }

    if (hasAnthropicKey) {
      try {
        return await this.anthropicProvider.generate(business, options);
      } catch (err) {
        console.warn('Anthropic generation failed, falling back to preset:', err.message);
      }
    }

    return await this.presetProvider.generate(business, options);
  }
}

export const contentGenerationService = new ContentGenerationService();
