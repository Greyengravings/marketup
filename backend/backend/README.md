# MarketUp Backend (FastAPI)

Real, tested backend — verified end-to-end in development (signup, login, JWT
refresh, business profile CRUD, AI content generation, post approve/reject all
confirmed working against a live server during development).

## Setup

```bash
cd backend
python3 -m venv venv
source venv/bin/activate        # Windows: venv\Scripts\activate
pip install -r requirements.txt

cp .env.example .env
# then edit .env and set:
#   JWT_SECRET_KEY=<a long random string>
#   ANTHROPIC_API_KEY=<your real Anthropic API key>   <-- required for live AI generation
```

Get an Anthropic API key at https://console.anthropic.com if you don't have one.

## Run

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

Visit http://localhost:8000/docs for interactive Swagger docs (auto-generated
by FastAPI) — you can test every endpoint from the browser.

## What's real vs. stubbed

| Feature | Status |
|---|---|
| Signup / Login / JWT + refresh | ✅ Real, tested |
| Business profile CRUD | ✅ Real, tested |
| AI content generation (`/ai/generate`) | ✅ Real — calls the actual Anthropic API. Needs `ANTHROPIC_API_KEY`. |
| Post approve/reject/list | ✅ Real, tested |
| Database | SQLite by default (zero setup). Swap `DATABASE_URL` in `.env` for Postgres in production — the SQLAlchemy models don't need to change. |
| Instagram/Facebook/WhatsApp publishing | ❌ Not included in this pass — needs a Meta Developer App + long-lived tokens. See the full spec's §11 (Meta OAuth Integration Flow) for the exact flow to implement next. |
| Razorpay subscriptions | ❌ Not included in this pass — needs a live Razorpay account + Plan. See §16 of the spec. |

## Known gotcha already fixed for you

`passlib`'s bcrypt version-detection code breaks with modern `bcrypt` releases
(a known upstream issue). This project hashes passwords directly with the
`bcrypt` package instead of going through `passlib`, which avoids the bug
entirely — don't reintroduce `passlib` for auth.

## Next steps to make this production-grade

1. Swap SQLite → PostgreSQL (`DATABASE_URL=postgresql://user:pass@host/db`), add Alembic migrations.
2. Add the Meta OAuth + publishing routers (spec §11–13).
3. Add the Razorpay subscription routes + webhook handler (spec §15–16).
4. Move `JWT_SECRET_KEY` and `ANTHROPIC_API_KEY` into a real secrets manager before deploying.
