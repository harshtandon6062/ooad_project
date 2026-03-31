const Database = require('better-sqlite3');
const path = require('path');

const dbPath = path.join(__dirname, '..', 'database.sqlite');
const db = new Database(dbPath);

// Enable WAL mode for better performance
db.pragma('journal_mode = WAL');
db.pragma('foreign_keys = ON');

// Create tables
db.exec(`
  CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role TEXT CHECK(role IN ('customer','agent','admin','claims_adjuster')) DEFAULT 'customer',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
  );

  CREATE TABLE IF NOT EXISTS policies (
    policy_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    type VARCHAR(50) NOT NULL,
    provider VARCHAR(100) DEFAULT 'InsureCo',
    premium REAL NOT NULL,
    coverage_amount REAL DEFAULT 0,
    start_date DATE,
    expiry_date DATE,
    status TEXT CHECK(status IN ('active','expired','cancelled','template')) DEFAULT 'template',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
  );

  CREATE TABLE IF NOT EXISTS quotes (
    quote_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    policy_id INTEGER,
    premium_estimate REAL NOT NULL,
    valid_until DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (policy_id) REFERENCES policies(policy_id)
  );

  CREATE TABLE IF NOT EXISTS payments (
    payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    policy_id INTEGER,
    amount REAL NOT NULL,
    status VARCHAR(50) CHECK(status IN ('pending','completed','failed')) DEFAULT 'pending',
    method VARCHAR(50) DEFAULT 'card',
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (policy_id) REFERENCES policies(policy_id)
  );

  CREATE TABLE IF NOT EXISTS claims (
    claim_id INTEGER PRIMARY KEY AUTOINCREMENT,
    policy_id INTEGER,
    user_id INTEGER,
    amount REAL NOT NULL,
    description TEXT,
    status VARCHAR(50) CHECK(status IN ('submitted','under_review','approved','rejected')) DEFAULT 'submitted',
    submitted_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    resolved_date DATETIME,
    FOREIGN KEY (policy_id) REFERENCES policies(policy_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
  );
`);

module.exports = db;
