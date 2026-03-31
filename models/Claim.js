const db = require('../config/db');

const Claim = {
  submit(policyId, userId, amount, description) {
    const stmt = db.prepare(
      'INSERT INTO claims (policy_id, user_id, amount, description, status) VALUES (?, ?, ?, ?, ?)'
    );
    return stmt.run(policyId, userId, amount, description, 'submitted').lastInsertRowid;
  },

  findById(id) {
    return db.prepare(
      `SELECT c.*, p.type as policy_type, u.name as user_name
       FROM claims c
       JOIN policies p ON c.policy_id = p.policy_id
       JOIN users u ON c.user_id = u.user_id
       WHERE c.claim_id = ?`
    ).get(id);
  },

  getByUserId(userId) {
    return db.prepare(
      `SELECT c.*, p.type as policy_type
       FROM claims c
       JOIN policies p ON c.policy_id = p.policy_id
       WHERE c.user_id = ?
       ORDER BY c.submitted_date DESC`
    ).all(userId);
  },

  getAll() {
    return db.prepare(
      `SELECT c.*, p.type as policy_type, u.name as user_name
       FROM claims c
       JOIN policies p ON c.policy_id = p.policy_id
       JOIN users u ON c.user_id = u.user_id
       ORDER BY c.submitted_date DESC`
    ).all();
  },

  updateStatus(claimId, status) {
    const resolvedDate = (status === 'approved' || status === 'rejected') ? new Date().toISOString() : null;
    db.prepare('UPDATE claims SET status = ?, resolved_date = ? WHERE claim_id = ?').run(status, resolvedDate, claimId);
  }
};

module.exports = Claim;
