from datetime import datetime, timezone

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.core.deps import get_current_user
from app.db import get_db
from app.models import Business, Post, User
from app.schemas import GenerateContentRequest, PostActionResponse, PostResponse
from app.services.ai_service import generate_post_content

router = APIRouter(tags=["ai-content"])


def _get_business_or_404(db: Session, user: User) -> Business:
    business = db.query(Business).filter(Business.user_id == user.id).first()
    if not business:
        raise HTTPException(status_code=404, detail="Complete business onboarding first")
    return business


@router.post("/ai/generate", response_model=PostResponse, status_code=201)
def generate_content(
    payload: GenerateContentRequest,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    business = _get_business_or_404(db, current_user)

    try:
        result = generate_post_content(business, occasion=payload.occasion)
    except RuntimeError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=502, detail=f"AI generation failed: {e}")

    post = Post(
        business_id=business.id,
        occasion=payload.occasion,
        caption=result["caption"],
        hashtags=result["hashtags"],
        cta=result["cta"],
        image_prompt=result["image_prompt"],
        ai_model_used=result["model_used"],
        status="pending",
    )
    db.add(post)
    db.commit()
    db.refresh(post)
    return post


@router.get("/businesses/me/posts", response_model=list[PostResponse])
def list_posts(
    status_filter: str | None = None,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    business = _get_business_or_404(db, current_user)
    query = db.query(Post).filter(Post.business_id == business.id)
    if status_filter:
        query = query.filter(Post.status == status_filter)
    return query.order_by(Post.created_at.desc()).all()


@router.post("/posts/{post_id}/approve", response_model=PostActionResponse)
def approve_post(
    post_id: str, db: Session = Depends(get_db), current_user: User = Depends(get_current_user)
):
    business = _get_business_or_404(db, current_user)
    post = db.query(Post).filter(Post.id == post_id, Post.business_id == business.id).first()
    if not post:
        raise HTTPException(status_code=404, detail="Post not found")
    post.status = "scheduled"
    post.approved_at = datetime.now(timezone.utc)
    db.commit()
    return PostActionResponse(id=post.id, status=post.status)


@router.post("/posts/{post_id}/reject", response_model=PostActionResponse)
def reject_post(
    post_id: str, db: Session = Depends(get_db), current_user: User = Depends(get_current_user)
):
    business = _get_business_or_404(db, current_user)
    post = db.query(Post).filter(Post.id == post_id, Post.business_id == business.id).first()
    if not post:
        raise HTTPException(status_code=404, detail="Post not found")
    post.status = "rejected"
    db.commit()
    return PostActionResponse(id=post.id, status=post.status)
