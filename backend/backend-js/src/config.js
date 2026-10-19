import 'dotenv/config';

export const config = {
  port: process.env.PORT || 8000,
  databasePath: process.env.DATABASE_PATH || './marketup.db',
  jwtSecret: process.env.JWT_SECRET_KEY || 'CHANGE_ME_IN_PRODUCTION',
  accessTokenExpiresIn: '15m',
  refreshTokenExpiresIn: '30d',
  anthropicApiKey: process.env.ANTHROPIC_API_KEY || '',
  anthropicModel: process.env.ANTHROPIC_MODEL || 'claude-sonnet-4-6',
  openaiApiKey: process.env.OPENAI_API_KEY || '',
  openaiModel: process.env.OPENAI_MODEL || 'gpt-4o-mini',
  mongodbUri: process.env.MONGODB_URI || '',
};
