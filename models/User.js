const db = require('../config/db');
const bcrypt = require('bcryptjs');

const User = {
  create(name, email, password, role = 'customer') {
    const hash = bcrypt.hashSync(password, 10);
    const stmt = db.prepare('INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)');
    const result = stmt.run(name, email, hash, role);
    return result.lastInsertRowid;
  },

  findByEmail(email) {
    return db.prepare('SELECT * FROM users WHERE email = ?').get(email);
  },

  findById(id) {
    return db.prepare('SELECT user_id, name, email, role, created_at FROM users WHERE user_id = ?').get(id);
  },

  verifyPassword(plainPassword, hashedPassword) {
    return bcrypt.compareSync(plainPassword, hashedPassword);
  },

  getAll() {
    return db.prepare('SELECT user_id, name, email, role, created_at FROM users').all();
  }
};

module.exports = User;
