const Policy = require('../models/Policy');
const Quote = require('../models/Quote');

const policyController = {
  // GET /policies — searchPolicy()
  searchPolicy(req, res) {
    const { type, maxPremium } = req.query;
    const filters = {};
    if (type) filters.type = type;
    if (maxPremium) filters.maxPremium = parseFloat(maxPremium);

    const policies = Policy.getTemplates(filters);
    res.render('policies', {
      user: req.session.user,
      policies,
      filters: { type: type || '', maxPremium: maxPremium || '' }
    });
  },

  // GET /policies/:id — getPolicyDetails()
  getPolicyDetails(req, res) {
    const policy = Policy.findById(req.params.id);
    if (!policy) {
      return res.status(404).render('error', { user: req.session.user, message: 'Policy not found' });
    }
    res.render('policy-detail', { user: req.session.user, policy });
  },

  // POST /policies/:id/quote — generateQuote()
  generateQuote(req, res) {
    const policy = Policy.findById(req.params.id);
    if (!policy) {
      return res.status(404).render('error', { user: req.session.user, message: 'Policy not found' });
    }

    // Simple premium calculation (base premium + random factor for demo)
    const ageFactor = req.body.age ? (parseInt(req.body.age) > 40 ? 1.3 : 1.0) : 1.0;
    const premiumEstimate = Math.round(policy.premium * ageFactor * 100) / 100;

    const validUntil = new Date();
    validUntil.setDate(validUntil.getDate() + 7);
    const validUntilStr = validUntil.toISOString().split('T')[0];

    const quoteId = Quote.create(req.session.user.user_id, policy.policy_id, premiumEstimate, validUntilStr);
    const quote = Quote.findById(quoteId);

    res.render('quote', { user: req.session.user, quote, policy });
  },

  // GET /my-policies — user's purchased policies
  myPolicies(req, res) {
    const policies = Policy.getByUserId(req.session.user.user_id);
    res.render('my-policies', { user: req.session.user, policies });
  },

  // Admin: GET /admin/policies
  adminPolicies(req, res) {
    const policies = Policy.getAll();
    res.render('admin-policies', { user: req.session.user, policies });
  },

  // Admin: POST /admin/policies — create template
  createTemplate(req, res) {
    const { type, provider, premium, coverageAmount } = req.body;
    Policy.createTemplate(type, provider || 'InsureCo', parseFloat(premium), parseFloat(coverageAmount));
    res.redirect('/admin/policies');
  }
};

module.exports = policyController;
