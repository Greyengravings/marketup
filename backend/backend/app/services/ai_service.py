import json
from datetime import date
from typing import Optional

import anthropic

from app.core.config import settings
from app.models import Business

SYSTEM_PROMPT = """You are MarketUp's AI social media copywriter for small Indian businesses.
You write concise, warm, locally-flavoured social media posts that match a business's brand voice.
You ALWAYS respond with ONLY a single valid JSON object, no markdown fences, no preamble, matching exactly:
{
  "caption": "string, 2-4 sentences, engaging, no hashtags inside it",
  "hashtags": ["#tag1", "#tag2", "... 5 to 8 relevant hashtags"],
  "cta": "short call to action, e.g. 'Visit us today!'",
  "image_prompt": "a descriptive prompt for generating a branded social media image for this post"
}
"""


def _build_user_prompt(business: Business, occasion: Optional[str]) -> str:
    occasion_line = (
        f"Occasion / theme to write about: {occasion}."
        if occasion
        else f"No specific occasion was given — pick something naturally relevant for "
        f"today ({date.today().isoformat()}) for this type of business (e.g. a seasonal "
        f"angle, a general trust/quality angle, or a light promotional angle)."
    )
    colors = ", ".join(business.brand_colors) if business.brand_colors else "not specified"
    return f"""Business name: {business.business_name}
Sector: {business.sector} / {business.sub_sector or "n/a"}
Description: {business.description or "n/a"}
Location/address: {business.address or "n/a"}
Brand colors: {colors}
{occasion_line}

Write one social media post for this business following the JSON schema exactly."""


def generate_post_content(business: Business, occasion: Optional[str] = None) -> dict:
    """Calls the real Anthropic API to generate a branded social media post.

    Requires ANTHROPIC_API_KEY to be set in the environment / .env file.
    """
    if not settings.anthropic_api_key:
        raise RuntimeError(
            "ANTHROPIC_API_KEY is not set. Add it to backend/.env to enable live AI generation."
        )

    client = anthropic.Anthropic(api_key=settings.anthropic_api_key)

    message = client.messages.create(
        model=settings.anthropic_model,
        max_tokens=600,
        system=SYSTEM_PROMPT,
        messages=[{"role": "user", "content": _build_user_prompt(business, occasion)}],
    )

    raw_text = "".join(
        block.text for block in message.content if getattr(block, "type", None) == "text"
    ).strip()

    # Defensive parsing in case the model wraps JSON in fences despite instructions
    if raw_text.startswith("```"):
        raw_text = raw_text.strip("`")
        if raw_text.startswith("json"):
            raw_text = raw_text[4:]
        raw_text = raw_text.strip()

    data = json.loads(raw_text)

    return {
        "caption": data["caption"],
        "hashtags": data["hashtags"],
        "cta": data["cta"],
        "image_prompt": data["image_prompt"],
        "model_used": settings.anthropic_model,
    }
