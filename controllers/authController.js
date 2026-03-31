const User = require('../models/User');

const authController = {
  // GET /login
  loginPage(req, res) {
    res.render('login', { user: req.session.user || null, error: null });
  },

  // POST /login
  login(req, res) {
    const { email, password } = req.body;
    const user = User.findByEmail(email);

    if (!user || !User.verifyPassword(password, user.password)) {
      return res.render('login', { user: null, error: 'Invalid email or password' });
    }

    req.session.user = {
      user_id: user.user_id,
      name: user.name,
      email: user.email,
      role: user.role
    };

    res.redirect('/dashboard');
  },

  // GET /register
  registerPage(req, res) {
    res.render('register', { user: req.session.user || null, error: null });
  },

  // POST /register
  register(req, res) {
    const { name, email, password, role } = req.body;

    try {
      const existing = User.findByEmail(email);
      if (existing) {
        return res.render('register', { user: null, error: 'Email already registered' });
      }

      const allowedRoles = ['customer', 'agent'];
      const userRole = allowedRoles.includes(role) ? role : 'customer';

      User.create(name, email, password, userRole);
      res.redirect('/login');
    } catch (err) {
      res.render('register', { user: null, error: 'Registration failed. Try again.' });
    }
  },

  // GET /logout
  logout(req, res) {
    req.session.destroy();
    res.redirect('/login');
  }
};

module.exports = authController;
