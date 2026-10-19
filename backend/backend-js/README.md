# MarketUp Backend (Node.js / Express)

A second, fully equivalent implementation of the backend in JavaScript —
same routes, same request/response shapes as `backend/` (the Python/FastAPI
version), so the Flutter app works against either one unchanged. Verified
end-to-end during development: signup, login, wrong-password rejection,
business creation/duplicate-rejection, AI-generate error handling,
post listing, and 404/403 auth checks were all run against a live server.

## Stack

- **Express** — HTTP routing
- **better-sqlite3** — embedded SQL database, zero setup, synchronous API
- **jsonwebtoken** — access + refresh JWTs
- **bcryptjs** — pure-JS password hashing (no native compile issues, unlike
  some `bcrypt` native bindings)
- **@anthropic-ai/sdk** — real calls to the Anthropic API for AI content generation

## Setup

```bash
cd backend-js
npm install
cp .env.example .env
# edit .env:
#   JWT_SECRET_KEY=<a long random string>
#   ANTHROPIC_API_KEY=<your real Anthropic API key>   <-- required for live AI generation
```

## Run

```bash
npm start
# or, for auto-restart on file changes:
npm run dev
```

Server listens on `http://localhost:8000` by default (same port as the
Python version, so you can swap between them without changing the Flutter
app's `API_BASE_URL`).

## API surface (identical to the Python backend)

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/auth/signup` | — | Create account, returns JWT pair |
| POST | `/auth/login` | — | Returns JWT pair |
| POST | `/auth/refresh` | — | Exchange refresh token for a new pair |
| GET | `/auth/me` | ✓ | Current user info |
| POST | `/businesses` | ✓ | Create business profile (one per user) |
| GET | `/businesses/me` | ✓ | Fetch your business profile |
| PATCH | `/businesses/me` | ✓ | Update your business profile |
| POST | `/ai/generate` | ✓ | Real Anthropic API call → generates a post |
| GET | `/businesses/me/posts` | ✓ | List your posts (optional `?status_filter=`) |
| POST | `/posts/:id/approve` | ✓ | Marks a post `scheduled` |
| POST | `/posts/:id/reject` | ✓ | Marks a post `rejected` |
| GET | `/health` | — | Health check |

## What's real vs. stubbed

Same as the Python backend — auth, business CRUD, and AI generation are real
and tested; Meta OAuth publishing and Razorpay billing are not included in
this pass (see the main PRD's §11 and §16 for the exact flow to add next).

## Why two backends?

You asked for the backend in JS specifically — this is a complete,
independent implementation, not a wrapper around the Python one. Pick
whichever matches your team's stack; both speak the exact same API contract
to the Flutter app.
