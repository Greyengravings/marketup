import uuid
from datetime import datetime, timezone

from sqlalchemy import Column, String, DateTime, ForeignKey, Text, JSON, Boolean
from sqlalchemy.orm import relationship

from app.db import Base


def gen_uuid() -> str:
    return str(uuid.uuid4())


def utcnow() -> datetime:
    return datetime.now(timezone.utc)


class User(Base):
    __tablename__ = "users"

    id = Column(String, primary_key=True, default=gen_uuid)
    email = Column(String, unique=True, nullable=False, index=True)
    password_hash = Column(String, nullable=False)
    status = Column(String, default="active")
    created_at = Column(DateTime, default=utcnow)

    business = relationship("Business", back_populates="owner", uselist=False)


class Business(Base):
    __tablename__ = "businesses"

    id = Column(String, primary_key=True, default=gen_uuid)
    user_id = Column(String, ForeignKey("users.id"), nullable=False)
    business_name = Column(String, nullable=False)
    owner_name = Column(String)
    sector = Column(String, nullable=False)
    sub_sector = Column(String)
    description = Column(Text)
    address = Column(String)
    phone = Column(String)
    email = Column(String)
    brand_colors = Column(JSON, default=list)
    logo_url = Column(String)
    website = Column(String)
    created_at = Column(DateTime, default=utcnow)

    owner = relationship("User", back_populates="business")
    posts = relationship("Post", back_populates="business")


class Post(Base):
    __tablename__ = "posts"

    id = Column(String, primary_key=True, default=gen_uuid)
    business_id = Column(String, ForeignKey("businesses.id"), nullable=False)
    occasion = Column(String)
    caption = Column(Text)
    hashtags = Column(JSON, default=list)
    cta = Column(String)
    image_prompt = Column(Text)
    status = Column(String, default="pending")  # pending, approved, rejected, scheduled
    ai_model_used = Column(String)
    created_at = Column(DateTime, default=utcnow)
    approved_at = Column(DateTime, nullable=True)

    business = relationship("Business", back_populates="posts")
