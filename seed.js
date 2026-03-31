/**
 * Seed script — populates the database with demo data
 * Run: node seed.js
 */
const db = require('./config/db');
const User = require('./models/User');
const Policy = require('./models/Policy');

console.log('🌱 Seeding database...\n');

// Clear existing data
db.exec('DELETE FROM claims');
db.exec('DELETE FROM payments');
db.exec('DELETE FROM quotes');
db.exec('DELETE FROM policies');
db.exec('DELETE FROM users');

// Reset auto-increment
db.exec("DELETE FROM sqlite_sequence WHERE name IN ('users','policies','quotes','payments','claims')");

// === Users ===
const users = [
  { name: 'Harsh (Customer)',   email: 'harsh@example.com',    password: 'password123', role: 'customer' },
  { name: 'Priya (Agent)',      email: 'priya@example.com',    password: 'password123', role: 'agent' },
  { name: 'Admin User',         email: 'admin@example.com',    password: 'admin123',    role: 'admin' },
  { name: 'Raj (Adjuster)',     email: 'raj@example.com',      password: 'password123', role: 'claims_adjuster' },
];

users.forEach(u => {
  User.create(u.name, u.email, u.password, u.role);
  console.log(`  ✅ User: ${u.email} (${u.role})`);
});

// === Policy Templates ===
const templates = [
  { type: 'vehicle_2w', provider: 'InsureCo',     premium: 1500, coverage: 100000 },
  { type: 'vehicle_2w', provider: 'SafeRide',     premium: 1800, coverage: 150000 },
  { type: 'vehicle_4w', provider: 'InsureCo',     premium: 5000, coverage: 500000 },
  { type: 'vehicle_4w', provider: 'AutoShield',   premium: 6500, coverage: 750000 },
  { type: 'health',     provider: 'HealthFirst',  premium: 3000, coverage: 300000 },
  { type: 'health',     provider: 'MediCare Plus', premium: 5500, coverage: 500000 },
];

templates.forEach(t => {
  Policy.createTemplate(t.type, t.provider, t.premium, t.coverage);
  console.log(`  ✅ Policy: ${t.type} by ${t.provider} — ₹${t.premium}/yr`);
});

console.log('\n🎉 Seed complete!\n');
console.log('Demo credentials:');
console.log('  Customer:  harsh@example.com / password123');
console.log('  Agent:     priya@example.com / password123');
console.log('  Admin:     admin@example.com / admin123');
console.log('  Adjuster:  raj@example.com   / password123');
console.log('\nRun: node app.js\n');
