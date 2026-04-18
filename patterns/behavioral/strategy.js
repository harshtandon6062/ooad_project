/**
 * STRATEGY PATTERN (Behavioral)
 * Created by: Dhruv
 * 
 * Purpose: Defines a family of algorithms, encapsulates each one, and makes them
 * interchangeable. Used for different premium calculation strategies.
 * 
 * Benefits:
 * - Easy to add new premium calculation methods
 * - Eliminates conditional logic in controllers
 * - Strategies can be swapped at runtime
 * - Each strategy is independently testable
 */

/**
 * PremiumCalculationStrategy - Base class for all premium calculation strategies
 */
class PremiumCalculationStrategy {
  /**
   * Calculate premium based on policy and user details
   * @param {Object} policy - Policy object
   * @param {Object} userData - User data (age, gender, health status, etc.)
   * @returns {number} Calculated premium
   */
  calculate(policy, userData) {
    throw new Error('calculate() must be implemented by subclasses');
  }
}

/**
 * StandardPremiumStrategy - Default premium calculation
 * Base premium with age factor adjustment
 */
class StandardPremiumStrategy extends PremiumCalculationStrategy {
  calculate(policy, userData) {
    let premium = policy.premium;
    
    // Age factor adjustment
    if (userData.age) {
      if (userData.age < 25) {
        premium *= 1.5; // Higher risk for young drivers/health issues
      } else if (userData.age >= 25 && userData.age < 50) {
        premium *= 1.0; // Standard rate
      } else if (userData.age >= 50 && userData.age < 65) {
        premium *= 1.3; // Slightly higher for seniors
      } else {
        premium *= 1.6; // Higher for 65+
      }
    }
    
    return Math.round(premium * 100) / 100;
  }
}

/**
 * PremiumPlusStrategy - Premium calculation with additional factors
 * Includes lifestyle, occupation, and risk assessments
 */
class PremiumPlusStrategy extends PremiumCalculationStrategy {
  calculate(policy, userData) {
    let premium = policy.premium;
    
    // Age factor
    const ageFactor = this.getAgeFactor(userData.age);
    premium *= ageFactor;
    
    // Lifestyle factor
    const lifestyleFactor = this.getLifestyleFactor(userData);
    premium *= lifestyleFactor;
    
    // Occupation risk factor
    const occupationFactor = this.getOccupationFactor(userData.occupation);
    premium *= occupationFactor;
    
    return Math.round(premium * 100) / 100;
  }
  
  getAgeFactor(age) {
    if (!age) return 1.0;
    if (age < 25) return 1.5;
    if (age < 50) return 1.0;
    if (age < 65) return 1.3;
    return 1.6;
  }
  
  getLifestyleFactor(userData) {
    let factor = 1.0;
    
    if (userData.smoker === true) {
      factor += 0.3; // 30% increase for smokers
    }
    
    if (userData.hasPreexistingConditions === true) {
      factor += 0.4; // 40% increase for pre-existing conditions
    }
    
    return factor;
  }
  
  getOccupationFactor(occupation) {
    const occupationRisks = {
      'construction': 1.4,
      'mining': 1.5,
      'manufacturing': 1.2,
      'office': 1.0,
      'retail': 1.05
    };
    
    return occupationRisks[occupation] || 1.0;
  }
}

/**
 * DiscountedPremiumStrategy - Premium calculation with volume/loyalty discounts
 * Applied for multiple policies, long-term customers, or bulk purchases
 */
class DiscountedPremiumStrategy extends PremiumCalculationStrategy {
  calculate(policy, userData) {
    let premium = policy.premium;
    
    // Base age adjustment
    const ageFactor = this.getAgeFactor(userData.age);
    premium *= ageFactor;
    
    // Apply discounts
    let discountFactor = 1.0;
    
    // Multi-policy discount
    if (userData.existingPoliciesCount && userData.existingPoliciesCount >= 2) {
      discountFactor -= 0.1; // 10% discount
    }
    
    // Loyalty discount (customer since date)
    if (userData.membershipMonths && userData.membershipMonths >= 12) {
      discountFactor -= 0.15; // 15% discount for 1+ year members
    }
    
    // Safety/claims record discount
    if (userData.noClaimsYears && userData.noClaimsYears >= 2) {
      discountFactor -= 0.2; // 20% discount for 2+ years without claims
    }
    
    premium *= discountFactor;
    
    return Math.round(premium * 100) / 100;
  }
  
  getAgeFactor(age) {
    if (!age) return 1.0;
    if (age < 25) return 1.5;
    if (age < 50) return 1.0;
    if (age < 65) return 1.3;
    return 1.6;
  }
}

/**
 * PremiumCalculator - Context class that uses strategies
 */
class PremiumCalculator {
  constructor(strategy = null) {
    this.strategy = strategy || new StandardPremiumStrategy();
  }
  
  /**
   * Set the calculation strategy
   * @param {PremiumCalculationStrategy} strategy - Strategy to use
   */
  setStrategy(strategy) {
    if (!(strategy instanceof PremiumCalculationStrategy)) {
      throw new Error('Strategy must be an instance of PremiumCalculationStrategy');
    }
    this.strategy = strategy;
  }
  
  /**
   * Calculate premium using current strategy
   * @param {Object} policy - Policy object
   * @param {Object} userData - User data
   * @returns {number} Calculated premium
   */
  calculatePremium(policy, userData) {
    return this.strategy.calculate(policy, userData);
  }
}

module.exports = {
  PremiumCalculationStrategy,
  StandardPremiumStrategy,
  PremiumPlusStrategy,
  DiscountedPremiumStrategy,
  PremiumCalculator
};
