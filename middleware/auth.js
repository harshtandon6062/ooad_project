// Authentication & Role-based access middleware

function isAuthenticated(req, res, next) {
  if (req.session && req.session.user) {
    return next();
  }
  return res.redirect('/login');
}

function authorize(...roles) {
  return (req, res, next) => {
    if (!req.session || !req.session.user) {
      return res.redirect('/login');
    }
    if (roles.length && !roles.includes(req.session.user.role)) {
      return res.status(403).render('error', {
        user: req.session.user,
        message: 'Access Denied: You do not have permission to view this page.'
      });
    }
    return next();
  };
}

module.exports = { isAuthenticated, authorize };
