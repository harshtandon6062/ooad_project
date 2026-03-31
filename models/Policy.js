const db = require('../config/db');

const Policy = {
  // Get all template policies (available for purchase)
  getTemplates(filters = {}) {
    let sql = 'SELECT * FROM policies WHERE status = ?';
    const params = ['template'];

    if (filters.type) {
      sql += ' AND type = ?';
      params.push(filters.type);
    }
    if (filters.maxPremium) {
      sql += ' AND premium <= ?';
      params.push(filters.maxPremium);
    }

    return db.prepare(sql).all(...params);
  },

  findById(id) {
    return db.prepare('SELECT * FROM policies WHERE policy_id = ?').get(id);
  },

  // Get policies owned by a user
  getByUserId(userId) {
    return db.prepare('SELECT * FROM policies WHERE user_id = ? AND status != ?').all(userId, 'template');
  },

  // Admin creates a policy template
  createTemplate(type, provider, premium, coverageAmount) {
    const stmt = db.prepare(
      'INSERT INTO policies (type, provider, premium, coverage_amount, status) VALUES (?, ?, ?, ?, ?)'
    );
    return stmt.run(type, provider, premium, coverageAmount, 'template').lastInsertRowid;
  },

  // Issue a policy to a user (from a template)
  issue(userId, type, provider, premium, coverageAmount, startDate, expiryDate) {
    const stmt = db.prepare(
      `INSERT INTO policies (user_id, type, provider, premium, coverage_amount, start_date, expiry_date, status)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?)`
    );
    return stmt.run(userId, type, provider, premium, coverageAmount, startDate, expiryDate, 'active').lastInsertRowid;
  },

  getAll() {
    return db.prepare('SELECT p.*, u.name as user_name FROM policies p LEFT JOIN users u ON p.user_id = u.user_id').all();
  }
};

module.exports = Policy;
