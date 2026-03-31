const db = require('../config/db');

const Quote = {
  create(userId, policyId, premiumEstimate, validUntil) {
    const stmt = db.prepare(
      'INSERT INTO quotes (user_id, policy_id, premium_estimate, valid_until) VALUES (?, ?, ?, ?)'
    );
    return stmt.run(userId, policyId, premiumEstimate, validUntil).lastInsertRowid;
  },

  findById(id) {
    return db.prepare(
      `SELECT q.*, p.type as policy_type, p.provider, p.coverage_amount
       FROM quotes q
       JOIN policies p ON q.policy_id = p.policy_id
       WHERE q.quote_id = ?`
    ).get(id);
  },

  getByUserId(userId) {
    return db.prepare(
      `SELECT q.*, p.type as policy_type, p.provider
       FROM quotes q
       JOIN policies p ON q.policy_id = p.policy_id
       WHERE q.user_id = ?
       ORDER BY q.created_at DESC`
    ).all(userId);
  }
};

module.exports = Quote;
