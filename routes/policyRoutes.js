const express = require('express');
const router = express.Router();
const policyController = require('../controllers/policyController');
const { isAuthenticated, authorize } = require('../middleware/auth');

// Customer routes
router.get('/policies', isAuthenticated, policyController.searchPolicy);
router.get('/policies/:id', isAuthenticated, policyController.getPolicyDetails);
router.post('/policies/:id/quote', isAuthenticated, policyController.generateQuote);
router.get('/my-policies', isAuthenticated, policyController.myPolicies);

// Admin routes
router.get('/admin/policies', authorize('admin'), policyController.adminPolicies);
router.post('/admin/policies', authorize('admin'), policyController.createTemplate);

module.exports = router;
