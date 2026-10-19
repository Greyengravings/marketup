import mongoose from 'mongoose';

const userLogSchema = new mongoose.Schema({
  email: { type: String, required: true },
  action: { type: String, enum: ['signup', 'login'], required: true },
  timestamp: { type: Date, default: Date.now },
  ip: { type: String },
  userAgent: { type: String }
});

export const UserLog = mongoose.model('UserLog', userLogSchema);
