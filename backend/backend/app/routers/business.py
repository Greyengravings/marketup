from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.core.deps import get_current_user
from app.db import get_db
from app.models import Business, User
from app.schemas import BusinessCreate, BusinessResponse

router = APIRouter(prefix="/businesses", tags=["businesses"])


@router.post("", response_model=BusinessResponse, status_code=201)
def create_business(
    payload: BusinessCreate,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    existing = db.query(Business).filter(Business.user_id == current_user.id).first()
    if existing:
        raise HTTPException(status_code=400, detail="Business profile already exists for this user")

    business = Business(user_id=current_user.id, **payload.model_dump())
    db.add(business)
    db.commit()
    db.refresh(business)
    return business


@router.get("/me", response_model=BusinessResponse)
def get_my_business(
    db: Session = Depends(get_db), current_user: User = Depends(get_current_user)
):
    business = db.query(Business).filter(Business.user_id == current_user.id).first()
    if not business:
        raise HTTPException(status_code=404, detail="No business profile found — complete onboarding first")
    return business


@router.patch("/me", response_model=BusinessResponse)
def update_my_business(
    payload: BusinessCreate,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user),
):
    business = db.query(Business).filter(Business.user_id == current_user.id).first()
    if not business:
        raise HTTPException(status_code=404, detail="No business profile found")
    for key, value in payload.model_dump().items():
        setattr(business, key, value)
    db.commit()
    db.refresh(business)
    return business
