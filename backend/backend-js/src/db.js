import Database from 'better-sqlite3';
import { config } from './config.js';

export const db = new Database(config.databasePath);
db.pragma('journal_mode = WAL');
db.pragma('foreign_keys = ON');

export function migrate() {
  db.exec(`
    CREATE TABLE IF NOT EXISTS users (
      id TEXT PRIMARY KEY,
      email TEXT UNIQUE NOT NULL,
      password_hash TEXT NOT NULL,
      status TEXT NOT NULL DEFAULT 'active',
      created_at TEXT NOT NULL DEFAULT (datetime('now'))
    );

    CREATE TABLE IF NOT EXISTS businesses (
      id TEXT PRIMARY KEY,
      user_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
      business_name TEXT NOT NULL,
      owner_name TEXT,
      sector TEXT NOT NULL,
      sub_sector TEXT,
      description TEXT,
      address TEXT,
      city TEXT,
      phone TEXT,
      whatsapp TEXT,
      email TEXT,
      brand_colors TEXT NOT NULL DEFAULT '[]',
      brand_personality TEXT,
      tone_of_voice TEXT,
      target_audience TEXT,
      preferred_cta TEXT,
      social_handles TEXT NOT NULL DEFAULT '{}',
      logo_url TEXT,
      website TEXT,
      created_at TEXT NOT NULL DEFAULT (datetime('now'))
    );

    -- Migration for existing businesses table
    PRAGMA table_info(businesses);
  `);

  // Surgical migrations for businesses table
  const businessColumns = db.prepare("PRAGMA table_info(businesses)").all();
  const businessColNames = businessColumns.map(c => c.name);

  const newBusinessCols = [
    { name: 'city', type: 'TEXT' },
    { name: 'whatsapp', type: 'TEXT' },
    { name: 'brand_personality', type: 'TEXT' },
    { name: 'tone_of_voice', type: 'TEXT' },
    { name: 'target_audience', type: 'TEXT' },
    { name: 'preferred_cta', type: 'TEXT' },
    { name: 'social_handles', type: "TEXT NOT NULL DEFAULT '{}'" }
  ];

  newBusinessCols.forEach(col => {
    if (!businessColNames.includes(col.name)) {
      db.exec(`ALTER TABLE businesses ADD COLUMN ${col.name} ${col.type}`);
    }
  });

  db.exec(`
    CREATE UNIQUE INDEX IF NOT EXISTS idx_businesses_user ON businesses(user_id);

    CREATE TABLE IF NOT EXISTS posts (
      id TEXT PRIMARY KEY,
      business_id TEXT NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,
      occasion TEXT,
      caption TEXT NOT NULL,
      hashtags TEXT NOT NULL DEFAULT '[]',
      cta TEXT NOT NULL,
      image_prompt TEXT NOT NULL,
      generated_image TEXT,
      reel_idea TEXT,
      carousel_slides TEXT NOT NULL DEFAULT '[]',
      status TEXT NOT NULL DEFAULT 'pending',
      ai_model_used TEXT NOT NULL,
      created_at TEXT NOT NULL DEFAULT (datetime('now')),
      approved_at TEXT
    );

    CREATE INDEX IF NOT EXISTS idx_posts_business ON posts(business_id, status);
  `);

  // Surgical migrations for posts table
  const postColumns = db.prepare("PRAGMA table_info(posts)").all();
  const postColNames = postColumns.map(c => c.name);

  if (!postColNames.includes('reel_idea')) {
    db.exec("ALTER TABLE posts ADD COLUMN reel_idea TEXT");
  }
  if (!postColNames.includes('carousel_slides')) {
    db.exec("ALTER TABLE posts ADD COLUMN carousel_slides TEXT NOT NULL DEFAULT '[]'");
  }
}
