import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import { config } from '../config.js';

const SALT_ROUNDS = 10;

export function hashPassword(password) {
  return bcrypt.hashSync(password, SALT_ROUNDS);
}

export function verifyPassword(plainPassword, hash) {
  return bcrypt.compareSync(plainPassword, hash);
}

export function createAccessToken(userId) {
  return jwt.sign({ sub: userId, type: 'access' }, config.jwtSecret, {
    expiresIn: config.accessTokenExpiresIn,
  });
}

export function createRefreshToken(userId) {
  return jwt.sign({ sub: userId, type: 'refresh' }, config.jwtSecret, {
    expiresIn: config.refreshTokenExpiresIn,
  });
}

export function issueTokenPair(userId) {
  return {
    access_token: createAccessToken(userId),
    refresh_token: createRefreshToken(userId),
    token_type: 'bearer',
  };
}

export function decodeToken(token) {
  return jwt.verify(token, config.jwtSecret);
}
