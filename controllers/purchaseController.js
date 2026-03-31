const Policy = require('../models/Policy');
const Payment = require('../models/Payment');
const Quote = require('../models/Quote');

const purchaseController = {
  // GET /purchase/:quoteId — createOrder() — show payment page
  createOrder(req, res) {
    const quote = Quote.findById(req.params.quoteId);
    if (!quote) {
      return res.status(404).render('error', { user: req.session.user, message: 'Quote not found' });
    }
    res.render('payment', { user: req.session.user, quote, error: null });
  },

  // POST /purchase/:quoteId/pay — processPayment() + issuePolicy()
  processPayment(req, res) {
    const quote = Quote.findById(req.params.quoteId);
    if (!quote) {
      return res.status(404).render('error', { user: req.session.user, message: 'Quote not found' });
    }

    const { paymentMethod } = req.body;
    const userId = req.session.user.user_id;

    // Issue the policy
    const startDate = new Date().toISOString().split('T')[0];
    const expiryDate = new Date();
    expiryDate.setFullYear(expiryDate.getFullYear() + 1);
    const expiryDateStr = expiryDate.toISOString().split('T')[0];

    const policyId = Policy.issue(
      userId,
      quote.policy_type,
      quote.provider,
      quote.premium_estimate,
      quote.coverage_amount,
      startDate,
      expiryDateStr
    );

    // Create payment record (simulated — always succeeds)
    const paymentId = Payment.create(userId, policyId, quote.premium_estimate, paymentMethod || 'card');
    Payment.updateStatus(paymentId, 'completed');

    res.render('purchase-success', {
      user: req.session.user,
      policyId,
      paymentId,
      amount: quote.premium_estimate,
      policyType: quote.policy_type
    });
  }
};

module.exports = purchaseController;
