const db = require('../config/db');

const Payment = {
  create(userId, policyId, amount, method = 'card') {
    const stmt = db.prepare(
      'INSERT INTO payments (user_id, policy_id, amount, method, status) VALUES (?, ?, ?, ?, ?)'
    );
    return stmt.run(userId, policyId, amount, method, 'pending').lastInsertRowid;
  },

  findById(id) {
    return db.prepare('SELECT * FROM payments WHERE payment_id = ?').get(id);
  },

  updateStatus(paymentId, status) {
    db.prepare('UPDATE payments SET status = ? WHERE payment_id = ?').run(status, paymentId);
  },

  getByUserId(userId) {
    return db.prepare(
      `SELECT pay.*, p.type as policy_type
       FROM payments pay
       JOIN policies p ON pay.policy_id = p.policy_id
       WHERE pay.user_id = ?
       ORDER BY pay.transaction_date DESC`
    ).all(userId);
  }
};

module.exports = Payment;
