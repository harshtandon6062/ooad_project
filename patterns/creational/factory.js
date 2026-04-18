/**
 * FACTORY PATTERN (Creational)
 * Created by: Suman
 * 
 * Purpose: Encapsulates the creation of complex domain objects (Policy, Claim, Quote)
 * without exposing creation logic to the client code.
 * 
 * Benefits:
 * - Centralized object creation logic
 * - Easy to extend with new policy/claim types
 * - Decouples creation from usage
 * - Simplifies controller code
 */

const Policy = require('../../models/Policy');
const Claim = require('../../models/Claim');
const Quote = require('../../models/Quote');

/**
 * PolicyFactory - Creates different types of insurance policies
 */
class PolicyFactory {
  /**
   * Create a policy template
   * @param {string} type - Policy type (health, auto, home, life)
   * @param {string} provider - Insurance provider name
   * @param {number} premium - Base premium amount
   * @param {number} coverageAmount - Coverage amount
   * @returns {number} Policy ID
   */
  static createTemplate(type, provider, premium, coverageAmount) {
    this.validatePolicyType(type);
    this.validatePremium(premium);
    this.validateCoverageAmount(coverageAmount);
    
    return Policy.createTemplate(type, provider, premium, coverageAmount);
  }

  /**
   * Issue a policy to a user
   * @param {number} userId - User ID
   * @param {string} type - Policy type
   * @param {string} provider - Insurance provider
   * @param {number} premium - Premium amount
   * @param {number} coverageAmount - Coverage amount
   * @param {string} startDate - Policy start date (YYYY-MM-DD)
   * @param {string} expiryDate - Policy expiry date (YYYY-MM-DD)
   * @returns {number} Policy ID
   */
  static issuePolicy(userId, type, provider, premium, coverageAmount, startDate, expiryDate) {
    this.validatePolicyType(type);
    this.validatePremium(premium);
    this.validateCoverageAmount(coverageAmount);
    this.validateDates(startDate, expiryDate);
    
    return Policy.issue(userId, type, provider, premium, coverageAmount, startDate, expiryDate);
  }

  /**
   * Get all policy templates (available for purchase)
   * @param {Object} filters - Filter criteria
   * @returns {Array} Array of policy templates
   */
  static getAvailablePolicies(filters = {}) {
    return Policy.getTemplates(filters);
  }

  /**
   * Get user's active policies
   * @param {number} userId - User ID
   * @returns {Array} Array of user's policies
   */
  static getUserPolicies(userId) {
    return Policy.getByUserId(userId);
  }

  /**
   * Validate policy type
   * @private
   */
  static validatePolicyType(type) {
    const validTypes = ['health', 'auto', 'home', 'life'];
    if (!validTypes.includes(type)) {
      throw new Error(`Invalid policy type: ${type}. Must be one of: ${validTypes.join(', ')}`);
    }
  }

  /**
   * Validate premium amount
   * @private
   */
  static validatePremium(premium) {
    if (premium <= 0) {
      throw new Error('Premium must be greater than 0');
    }
    if (premium > 100000) {
      throw new Error('Premium cannot exceed 100,000');
    }
  }

  /**
   * Validate coverage amount
   * @private
   */
  static validateCoverageAmount(coverageAmount) {
    if (coverageAmount < 0) {
      throw new Error('Coverage amount cannot be negative');
    }
  }

  /**
   * Validate dates
   * @private
   */
  static validateDates(startDate, expiryDate) {
    const start = new Date(startDate);
    const expiry = new Date(expiryDate);
    
    if (expiry <= start) {
      throw new Error('Expiry date must be after start date');
    }
  }
}

/**
 * ClaimFactory - Creates and manages insurance claims
 */
class ClaimFactory {
  /**
   * Submit a new claim
   * @param {number} policyId - Policy ID
   * @param {number} userId - User ID
   * @param {number} amount - Claim amount
   * @param {string} description - Claim description
   * @returns {number} Claim ID
   */
  static submitClaim(policyId, userId, amount, description) {
    this.validateClaimAmount(amount);
    this.validateDescription(description);
    
    return Claim.submit(policyId, userId, amount, description);
  }

  /**
   * Get user's claims
   * @param {number} userId - User ID
   * @returns {Array} Array of claims
   */
  static getUserClaims(userId) {
    return Claim.getByUserId(userId);
  }

  /**
   * Get all claims (admin view)
   * @returns {Array} Array of all claims
   */
  static getAllClaims() {
    return Claim.getAll();
  }

  /**
   * Update claim status
   * @param {number} claimId - Claim ID
   * @param {string} status - New status (under_review, approved, rejected)
   */
  static updateClaimStatus(claimId, status) {
    this.validateStatus(status);
    Claim.updateStatus(claimId, status);
  }

  /**
   * Validate claim amount
   * @private
   */
  static validateClaimAmount(amount) {
    if (amount <= 0) {
      throw new Error('Claim amount must be greater than 0');
    }
    if (amount > 10000000) {
      throw new Error('Claim amount cannot exceed 10,000,000');
    }
  }

  /**
   * Validate description
   * @private
   */
  static validateDescription(description) {
    if (!description || description.trim().length < 10) {
      throw new Error('Description must be at least 10 characters long');
    }
  }

  /**
   * Validate status
   * @private
   */
  static validateStatus(status) {
    const validStatuses = ['submitted', 'under_review', 'approved', 'rejected'];
    if (!validStatuses.includes(status)) {
      throw new Error(`Invalid status: ${status}. Must be one of: ${validStatuses.join(', ')}`);
    }
  }
}

/**
 * QuoteFactory - Creates quotations for insurance policies
 */
class QuoteFactory {
  /**
   * Create a quote for a policy
   * @param {number} userId - User ID
   * @param {number} policyId - Policy ID
   * @param {number} premium - Premium estimate
   * @param {string} validUntil - Quote validity date (YYYY-MM-DD)
   * @returns {number} Quote ID
   */
  static createQuote(userId, policyId, premium, validUntil) {
    this.validatePremium(premium);
    this.validateValidityDate(validUntil);
    
    return Quote.create(userId, policyId, premium, validUntil);
  }

  /**
   * Get user's quotes
   * @param {number} userId - User ID
   * @returns {Array} Array of quotes
   */
  static getUserQuotes(userId) {
    return Quote.getByUserId(userId);
  }

  /**
   * Find quote by ID
   * @param {number} quoteId - Quote ID
   * @returns {Object} Quote object
   */
  static getQuote(quoteId) {
    return Quote.findById(quoteId);
  }

  /**
   * Validate premium amount
   * @private
   */
  static validatePremium(premium) {
    if (premium <= 0) {
      throw new Error('Premium must be greater than 0');
    }
  }

  /**
   * Validate validity date
   * @private
   */
  static validateValidityDate(validUntil) {
    const validity = new Date(validUntil);
    const now = new Date();
    
    if (validity <= now) {
      throw new Error('Quote must be valid in the future');
    }
  }
}

module.exports = {
  PolicyFactory,
  ClaimFactory,
  QuoteFactory
};
