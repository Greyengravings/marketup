from datetime import datetime
from typing import List, Optional

from pydantic import BaseModel, EmailStr, Field


# ---------- Auth ----------
class SignupRequest(BaseModel):
    email: EmailStr
    password: str = Field(min_length=8)


class LoginRequest(BaseModel):
    email: EmailStr
    password: str


class TokenResponse(BaseModel):
    access_token: str
    refresh_token: str
    token_type: str = "bearer"


class RefreshRequest(BaseModel):
    refresh_token: str


# ---------- Business ----------
class BusinessCreate(BaseModel):
    business_name: str
    owner_name: Optional[str] = None
    sector: str
    sub_sector: Optional[str] = None
    description: Optional[str] = None
    address: Optional[str] = None
    phone: Optional[str] = None
    email: Optional[EmailStr] = None
    brand_colors: List[str] = []
    website: Optional[str] = None


class BusinessResponse(BusinessCreate):
    id: str
    logo_url: Optional[str] = None
    created_at: datetime

    class Config:
        from_attributes = True


# ---------- AI Content ----------
class GenerateContentRequest(BaseModel):
    occasion: Optional[str] = None  # e.g. "Diwali", "Weekend Special", None = auto-pick


class PostResponse(BaseModel):
    id: str
    business_id: str
    occasion: Optional[str]
    caption: str
    hashtags: List[str]
    cta: str
    image_prompt: str
    status: str
    ai_model_used: str
    created_at: datetime

    class Config:
        from_attributes = True


class PostActionResponse(BaseModel):
    id: str
    status: str
