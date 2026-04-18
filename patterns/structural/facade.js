/**
 * FACADE PATTERN (Structural)
 * Created by: Harsh
 * 
 * Purpose: Provides a unified, simplified interface to a complex subsystem.
 * Hides interactions between controllers, models, and business logic.
 * 
 * Benefits:
 * - Simplifies controller code
 * - Decouples controllers from models
 * - Centralizes business logic
 * - Easy to maintain and extend
 * - Acts as a single point of contact for complex operations
 */

const User = require('../../models/User');
const Policy = require('../../models/Policy');
const Quote = require('../../models/Quote');
const Payment = require('../../models/Payment');
const Claim = require('../../models/Claim');
const { PolicyFactory, ClaimFactory, QuoteFactory } = require('../creational/factory');
const { StandardPremiumStrategy, PremiumCalculator } = require('../behavioral/strategy');
const { PolicyContext, ClaimContext } = require('../behavioral/state');

/**
 * InsuranceFacade - Main facade for all insurance operations
 * Provides a simplified interface to the insurance system
 */
class InsuranceFacade {
  /**
   * User Management Operations
   */

  /**
   * Register a new user
   * @param {string} name - User's name
   * @param {string} email - User's email
   * @param {string} password - User's password
   * @param {string} role - User's role (customer, agent, admin)
   * @returns {Object} Registration result { success, userId, message }
   */
  static registerUser(name, email, password, role = 'customer') {
    try {
      // Check if user already exists
      const existing = User.findByEmail(email);
      if (existing) {
        return { success: false, message: 'Email already registered' };
      }

      // Validate role
      const allowedRoles = ['customer', 'agent'];
      const userRole = allowedRoles.includes(role) ? role : 'customer';

      // Create user
      const userId = User.create(name, email, password, userRole);
      return {
        success: true,
        userId,
        message: 'User registered successfully'
      };
    } catch (error) {
      return { success: false, message: error.message };
    }
  }

  /**
   * Authenticate user
   * @param {string} email - User's email
   * @param {string} password - User's password
   * @returns {Object} Authentication result { success, user, message }
   */
  static authenticateUser(email, password) {
    try {
      const user = User.findByEmail(email);

      if (!user || !User.verifyPassword(password, user.password)) {
        return { success: false, message: 'Invalid email or password' };
      }

      return {
        success: true,
        user: {
          user_id: user.user_id,
          name: user.name,
          email: user.email,
          role: user.role
        }
      };
    } catch (error) {
      return { success: false, message: error.message };
    }
  }

  /**
   * Get user profile
   * @param {number} userId - User ID
   * @returns {Object} User profile
   */
  static getUserProfile(userId) {
    return User.findById(userId);
  }

  /**
   * Policy Management Operations
   */

  /**
   * Search available policies with filters
   * @param {Object} filters - Search filters { type, maxPremium }
   * @returns {Array} Array of available policies
   */
  static searchAvailablePolicies(filters = {}) {
    try {
      return PolicyFactory.getAvailablePolicies(filters);
    } catch (error) {
      console.error('Error searching policies:', error.message);
      return [];
    }
  }

  /**
   * Get policy details
   * @param {number} policyId - Policy ID
   * @returns {Object} Policy details
   */
  static getPolicyDetails(policyId) {
    try {
      return Policy.findById(policyId);
    } catch (error) {
      console.error('Error getting policy details:', error.message);
      return null;
    }
  }

  /**
   * Get user's purchased policies
   * @param {number} userId - User ID
   * @returns {Array} Array of user policies
   */
  static getUserPolicies(userId) {
    try {
      return PolicyFactory.getUserPolicies(userId);
    } catch (error) {
      console.error('Error getting user policies:', error.message);
      return [];
    }
  }

  /**
   * Create a policy template (Admin operation)
   * @param {string} type - Policy type
   * @param {string} provider - Provider name
   * @param {number} premium - Premium amount
   * @param {number} coverageAmount - Coverage amount
   * @returns {Object} Creation result { success, policyId, message }
   */
  static createPolicyTemplate(type, provider, premium, coverageAmount) {
    try {
      const policyId = PolicyFactory.createTemplate(type, provider, premium, coverageAmount);
      return {
        success: true,
        policyId,
        message: 'Policy template created successfully'
      };
    } catch (error) {
      return { success: false, message: error.message };
    }
  }

  /**
   * Quote Operations
   */

  /**
   * Generate a quote for a policy
   * @param {number} userId - User ID
   * @param {number} policyId - Policy ID
   * @param {Object} userData - User data for calculation (age, etc.)
   * @returns {Object} Quote result { success, quote, premiumEstimate, message }
   */
  static generateQuote(userId, policyId, userData = {}) {
    try {
      const policy = Policy.findById(policyId);

      if (!policy) {
        return { success: false, message: 'Policy not found' };
      }

      // Calculate premium using Strategy pattern
      const calculator = new PremiumCalculator(new StandardPremiumStrategy());
      const premiumEstimate = calculator.calculatePremium(policy, userData);

      // Create quote validity date (7 days from now)
      const validUntil = new Date();
      validUntil.setDate(validUntil.getDate() + 7);
      const validUntilStr = validUntil.toISOString().split('T')[0];

      // Create quote
      const quoteId = QuoteFactory.createQuote(userId, policyId, premiumEstimate, validUntilStr);
      const quote = Quote.findById(quoteId);

      return {
        success: true,
        quote,
        premiumEstimate,
        message: 'Quote generated successfully'
      };
    } catch (error) {
      return { success: false, message: error.message };
    }
  }

  /**
   * Purchase & Payment Operations
   */

  /**
   * Process policy purchase and payment
   * @param {number} userId - User ID
   * @param {number} quoteId - Quote ID
   * @param {string} paymentMethod - Payment method (card, upi, net_banking)
   * @returns {Object} Purchase result { success, policyId, paymentId, message }
   */
  static processPurchase(userId, quoteId, paymentMethod = 'card') {
    try {
      const quote = Quote.findById(quoteId);

      if (!quote) {
        return { success: false, message: 'Quote not found' };
      }

      // Issue policy
      const startDate = new Date().toISOString().split('T')[0];
      const expiryDate = new Date();
      expiryDate.setFullYear(expiryDate.getFullYear() + 1);
      const expiryDateStr = expiryDate.toISOString().split('T')[0];

      const policyId = PolicyFactory.issuePolicy(
        userId,
        quote.policy_type,
        quote.provider,
        quote.premium_estimate,
        quote.coverage_amount,
        startDate,
        expiryDateStr
      );

      // Create payment record
      const paymentId = Payment.create(userId, policyId, quote.premium_estimate, paymentMethod);
      Payment.updateStatus(paymentId, 'completed');

      return {
        success: true,
        policyId,
        paymentId,
        amount: quote.premium_estimate,
        policyType: quote.policy_type,
        message: 'Purchase completed successfully'
      };
    } catch (error) {
      return { success: false, message: error.message };
    }
  }

  /**
   * Claim Operations
   */

  /**
   * Submit a new claim
   * @param {number} userId - User ID
   * @param {number} policyId - Policy ID
   * @param {number} amount - Claim amount
   * @param {string} description - Claim description
   * @returns {Object} Claim result { success, claimId, message }
   */
  static submitClaim(userId, policyId, amount, description) {
    try {
      // Verify policy belongs to user
      const policy = Policy.findById(policyId);
      if (!policy || policy.user_id !== userId) {
        return { success: false, message: 'Invalid policy selected' };
      }

      // Check if policy is active (using State pattern)
      const policyContext = new PolicyContext(policyId, policy.status);
      try {
        policyContext.fileClaim();
      } catch (error) {
        return { success: false, message: error.message };
      }

      // Submit claim
      const claimId = ClaimFactory.submitClaim(policyId, userId, amount, description);

      return {
        success: true,
        claimId,
        message: 'Claim submitted successfully'
      };
    } catch (error) {
      return { success: false, message: error.message };
    }
  }

  /**
   * Get user's claims
   * @param {number} userId - User ID
   * @returns {Array} Array of user claims
   */
  static getUserClaims(userId) {
    try {
      return ClaimFactory.getUserClaims(userId);
    } catch (error) {
      console.error('Error getting user claims:', error.message);
      return [];
    }
  }

  /**
   * Get all claims (Admin/Adjuster operation)
   * @returns {Array} Array of all claims
   */
  static getAllClaims() {
    try {
      return ClaimFactory.getAllClaims();
    } catch (error) {
      console.error('Error getting all claims:', error.message);
      return [];
    }
  }

  /**
   * Update claim status (Admin/Adjuster operation)
   * @param {number} claimId - Claim ID
   * @param {string} status - New status (under_review, approved, rejected)
   * @returns {Object} Update result { success, message }
   */
  static updateClaimStatus(claimId, status) {
    try {
      const claim = Claim.findById(claimId);
      
      if (!claim) {
        return { success: false, message: 'Claim not found' };
      }

      // Use State pattern to validate transition
      const claimContext = new ClaimContext(claimId, claim.status);
      
      if (status === 'under_review') {
        claimContext.startReview();
      } else if (status === 'approved') {
        claimContext.approve();
      } else if (status === 'rejected') {
        claimContext.reject('Claims adjuster decision');
      } else {
        return { success: false, message: 'Invalid status' };
      }

      // Update in database
      ClaimFactory.updateClaimStatus(claimId, status);

      return {
        success: true,
        message: `Claim status updated to ${status}`
      };
    } catch (error) {
      return { success: false, message: error.message };
    }
  }

  /**
   * Get claim details
   * @param {number} claimId - Claim ID
   * @returns {Object} Claim details
   */
  static getClaimDetails(claimId) {
    try {
      return Claim.findById(claimId);
    } catch (error) {
      console.error('Error getting claim details:', error.message);
      return null;
    }
  }

  /**
   * Dashboard Statistics
   */

  /**
   * Get owner's dashboard data
   * @param {number} userId - User ID
   * @returns {Object} Dashboard data with stats
   */
  static getUserDashboard(userId) {
    try {
      const policies = this.getUserPolicies(userId);
      const claims = this.getUserClaims(userId);

      return {
        totalPolicies: policies.length,
        activePolicies: policies.filter(p => p.status === 'active').length,
        totalClaims: claims.length,
        approvedClaims: claims.filter(c => c.status === 'approved').length,
        pendingClaims: claims.filter(c => c.status === 'submitted' || c.status === 'under_review').length
      };
    } catch (error) {
      console.error('Error getting dashboard data:', error.message);
      return {};
    }
  }
}

module.exports = InsuranceFacade;
