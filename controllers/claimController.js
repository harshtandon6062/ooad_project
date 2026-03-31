const Claim = require('../models/Claim');
const Policy = require('../models/Policy');

const claimController = {
  // GET /claims/new — show claim submission form
  submitClaimPage(req, res) {
    const policies = Policy.getByUserId(req.session.user.user_id);
    res.render('claim-submit', { user: req.session.user, policies, error: null });
  },

  // POST /claims — submitClaim()
  submitClaim(req, res) {
    const { policyId, amount, description } = req.body;
    const userId = req.session.user.user_id;

    const policy = Policy.findById(policyId);
    if (!policy || policy.user_id !== userId) {
      const policies = Policy.getByUserId(userId);
      return res.render('claim-submit', {
        user: req.session.user,
        policies,
        error: 'Invalid policy selected'
      });
    }

    Claim.submit(parseInt(policyId), userId, parseFloat(amount), description);
    res.redirect('/claims');
  },

  // GET /claims — trackClaim() — list user's claims
  trackClaim(req, res) {
    const claims = Claim.getByUserId(req.session.user.user_id);
    res.render('claims', { user: req.session.user, claims });
  },

  // GET /adjuster/claims — all claims (for adjuster/admin)
  allClaims(req, res) {
    const claims = Claim.getAll();
    res.render('claim-review', { user: req.session.user, claims });
  },

  // POST /adjuster/claims/:id — updateClaimStatus()
  updateClaimStatus(req, res) {
    const { status } = req.body;
    const validStatuses = ['under_review', 'approved', 'rejected'];
    if (!validStatuses.includes(status)) {
      return res.redirect('/adjuster/claims');
    }
    Claim.updateStatus(req.params.id, status);
    res.redirect('/adjuster/claims');
  }
};

module.exports = claimController;
