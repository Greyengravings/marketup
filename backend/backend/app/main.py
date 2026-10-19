from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.db import Base, engine
from app.routers import ai_content, auth, business

Base.metadata.create_all(bind=engine)

app = FastAPI(title="MarketUp API", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # tighten this in production
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(auth.router)
app.include_router(business.router)
app.include_router(ai_content.router)


@app.get("/health")
def health():
    return {"status": "ok"}
