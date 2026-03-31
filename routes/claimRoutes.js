const express = require('express');
const router = express.Router();
const claimController = require('../controllers/claimController');
const { isAuthenticated, authorize } = require('../middleware/auth');

// Customer routes
router.get('/claims/new', isAuthenticated, claimController.submitClaimPage);
router.post('/claims', isAuthenticated, claimController.submitClaim);
router.get('/claims', isAuthenticated, claimController.trackClaim);

// Adjuster/Admin routes
router.get('/adjuster/claims', authorize('admin', 'claims_adjuster'), claimController.allClaims);
router.post('/adjuster/claims/:id', authorize('admin', 'claims_adjuster'), claimController.updateClaimStatus);

module.exports = router;
