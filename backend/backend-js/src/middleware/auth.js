import { db } from '../db.js';
import { decodeToken } from '../utils/security.js';

export function requireAuth(req, res, next) {
  const authHeader = req.headers['authorization'] || '';
  const token = authHeader.startsWith('Bearer ') ? authHeader.slice(7) : null;

  if (!token) {
    return res.status(403).json({ detail: 'Not authenticated' });
  }

  let payload;
  try {
    payload = decodeToken(token);
    if (payload.type !== 'access') throw new Error('wrong token type');
  } catch (err) {
    return res.status(401).json({ detail: 'Invalid or expired token' });
  }

  const user = db.prepare('SELECT * FROM users WHERE id = ?').get(payload.sub);
  if (!user) {
    return res.status(401).json({ detail: 'User not found' });
  }

  req.user = user;
  next();
}
