const express = require('express');
const session = require('express-session');
const path = require('path');

// Initialize database (creates tables on first run)
require('./config/db');

const app = express();
const PORT = process.env.PORT || 3000;

// View engine
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

// Middleware
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// Session
app.use(session({
  secret: 'insureflow-secret-key-2026',
  resave: false,
  saveUninitialized: false,
  cookie: { maxAge: 24 * 60 * 60 * 1000 } // 24 hours
}));

// Routes
const authRoutes = require('./routes/authRoutes');
const policyRoutes = require('./routes/policyRoutes');
const purchaseRoutes = require('./routes/purchaseRoutes');
const claimRoutes = require('./routes/claimRoutes');

app.use('/', authRoutes);
app.use('/', policyRoutes);
app.use('/', purchaseRoutes);
app.use('/', claimRoutes);

// Dashboard route
const { isAuthenticated } = require('./middleware/auth');
app.get('/dashboard', isAuthenticated, (req, res) => {
  res.render('dashboard', { user: req.session.user });
});

// Root redirect
app.get('/', (req, res) => {
  if (req.session && req.session.user) {
    return res.redirect('/dashboard');
  }
  res.redirect('/login');
});

// 404
app.use((req, res) => {
  res.status(404).render('error', {
    user: req.session ? req.session.user : null,
    message: 'Page not found'
  });
});

// Start server
app.listen(PORT, () => {
  console.log(`\n  🛡️  InsureFlow running at http://localhost:${PORT}\n`);
});
