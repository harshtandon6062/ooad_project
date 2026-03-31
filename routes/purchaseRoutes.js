const express = require('express');
const router = express.Router();
const purchaseController = require('../controllers/purchaseController');
const { isAuthenticated } = require('../middleware/auth');

router.get('/purchase/:quoteId', isAuthenticated, purchaseController.createOrder);
router.post('/purchase/:quoteId/pay', isAuthenticated, purchaseController.processPayment);

module.exports = router;
