import { Router } from 'express';
import { v4 as uuidv4 } from 'uuid';
import { db } from '../db.js';
import { requireAuth } from '../middleware/auth.js';
import { UserLog } from '../models/userLog.js';
import {
  createAccessToken,
  createRefreshToken,
  decodeToken,
  hashPassword,
  issueTokenPair,
  verifyPassword,
} from '../utils/security.js';

export const authRouter = Router();

const logUserAction = async (req, email, action) => {
  try {
    await UserLog.create({
      email,
      action,
      ip: req.ip,
      userAgent: req.headers['user-agent']
    });
  } catch (err) {
    console.error('Failed to log user action to MongoDB:', err.message);
  }
};

authRouter.post('/signup', async (req, res) => {
  const { email, password } = req.body || {};
  if (!email || !password || password.length < 8) {
    return res.status(422).json({
      detail: 'A valid email and a password of at least 8 characters are required',
    });
  }

  const existing = db.prepare('SELECT id FROM users WHERE email = ?').get(email);
  if (existing) {
    return res.status(400).json({ detail: 'An account with this email already exists' });
  }

  const id = uuidv4();
  db.prepare(
    'INSERT INTO users (id, email, password_hash) VALUES (?, ?, ?)',
  ).run(id, email, hashPassword(password));

  await logUserAction(req, email, 'signup');

  return res.status(201).json(issueTokenPair(id));
});

authRouter.post('/login', async (req, res) => {
  const { email, password } = req.body || {};
  const user = db.prepare('SELECT * FROM users WHERE email = ?').get(email);
  if (!user || !verifyPassword(password || '', user.password_hash)) {
    return res.status(401).json({ detail: 'Invalid email or password' });
  }

  await logUserAction(req, email, 'login');

  return res.json(issueTokenPair(user.id));
});

authRouter.post('/refresh', (req, res) => {
  const { refresh_token: refreshToken } = req.body || {};
  if (!refreshToken) {
    return res.status(401).json({ detail: 'Invalid or expired refresh token' });
  }

  let payload;
  try {
    payload = decodeToken(refreshToken);
    if (payload.type !== 'refresh') throw new Error('wrong token type');
  } catch (err) {
    return res.status(401).json({ detail: 'Invalid or expired refresh token' });
  }

  const user = db.prepare('SELECT * FROM users WHERE id = ?').get(payload.sub);
  if (!user) {
    return res.status(401).json({ detail: 'User not found' });
  }

  return res.json(issueTokenPair(user.id));
});

authRouter.get('/me', requireAuth, (req, res) => {
  res.json({ id: req.user.id, email: req.user.email, status: req.user.status });
});
