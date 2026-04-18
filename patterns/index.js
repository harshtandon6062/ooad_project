/**
 * DESIGN PATTERNS - INDEX
 * 
 * This directory contains implementations of 4 core design patterns
 * divided across creational, structural, and behavioral categories.
 * 
 * Each pattern solves specific problems in the insurance management system.
 */

// ==================== CREATIONAL PATTERNS ====================

/**
 * FACTORY PATTERN (Suman)
 * File: creational/factory.js
 * 
 * Encapsulates the creation of complex objects (Policy, Claim, Quote).
 * Provides factory methods that handle validation and object initialization.
 * 
 * Classes:
 * - PolicyFactory: Creates and manages policy instances
 * - ClaimFactory: Creates and manages claim instances
 * - QuoteFactory: Creates and manages quote instances
 * 
 * Benefits:
 * - Centralized creation logic
 * - Consistent validation
 * - Easy to extend for new object types
 * - Decouples object creation from usage
 */
const {
  PolicyFactory,
  ClaimFactory,
  QuoteFactory
} = require('./creational/factory');

// ==================== STRUCTURAL PATTERNS ====================

/**
 * FACADE PATTERN (Harsh)
 * File: structural/facade.js
 * 
 * Provides a unified, simplified interface to the complex insurance subsystem.
 * Hides interactions between controllers, models, factories, and strategies.
 * Serves as the single point of contact for business logic operations.
 * 
 * Main Class:
 * - InsuranceFacade: Unified interface for all operations
 *   - User management (register, authenticate)
 *   - Policy operations (search, create, purchase)
 *   - Quote generation
 *   - Payment processing
 *   - Claim operations (submit, track, update)
 *   - Dashboard statistics
 * 
 * Benefits:
 * - Simplifies controller code
 * - Centralizes business logic
 * - Improves maintainability
 * - Reduces coupling between layers
 */
const InsuranceFacade = require('./structural/facade');

// ==================== BEHAVIORAL PATTERNS ====================

/**
 * STATE PATTERN (Dishan)
 * File: behavioral/state.js
 * 
 * Manages the lifecycle and state transitions of Policies and Claims.
 * Encapsulates state-specific behavior and validates legal transitions.
 * 
 * Policy States:
 * - ActivePolicyState: Policy is active and valid
 * - ExpiredPolicyState: Policy has expired
 * - CancelledPolicyState: Policy has been cancelled
 * - SuspendedPolicyState: Policy is temporarily suspended
 * 
 * Claim States:
 * - SubmittedClaimState: Claim has been submitted
 * - UnderReviewClaimState: Claim is being reviewed
 * - ApprovedClaimState: Claim has been approved
 * - RejectedClaimState: Claim has been rejected
 * - PaidClaimState: Claim has been paid
 * 
 * Context Classes:
 * - PolicyContext: Manages policy state and transitions
 * - ClaimContext: Manages claim state and transitions
 * 
 * Benefits:
 * - Eliminates complex conditional logic
 * - Encapsulates state-specific behavior
 * - Ensures valid state transitions
 * - Easy to add new states
 */
const {
  PolicyContext,
  ActivePolicyState,
  ExpiredPolicyState,
  CancelledPolicyState,
  SuspendedPolicyState,
  ClaimContext,
  SubmittedClaimState,
  UnderReviewClaimState,
  ApprovedClaimState,
  RejectedClaimState,
  PaidClaimState
} = require('./behavioral/state');

/**
 * STRATEGY PATTERN (Dhruv)
 * File: behavioral/strategy.js
 * 
 * Defines different premium calculation algorithms and makes them interchangeable.
 * Strategies can be swapped at runtime without changing client code.
 * 
 * Strategy Classes:
 * - StandardPremiumStrategy: Base premium with age factor
 * - PremiumPlusStrategy: Premium with lifestyle and occupation factors
 * - DiscountedPremiumStrategy: Premium with loyalty and claims discounts
 * 
 * Context Class:
 * - PremiumCalculator: Uses strategies to calculate premiums
 * 
 * Benefits:
 * - Easy to add new calculation methods
 * - Eliminates conditional logic in controllers
 * - Strategies are independently testable
 * - Runtime strategy selection
 */
const {
  PremiumCalculationStrategy,
  StandardPremiumStrategy,
  PremiumPlusStrategy,
  DiscountedPremiumStrategy,
  PremiumCalculator
} = require('./behavioral/strategy');

// ==================== EXPORTS ====================

module.exports = {
  // Creational
  PolicyFactory,
  ClaimFactory,
  QuoteFactory,

  // Structural
  InsuranceFacade,

  // Behavioral
  PolicyContext,
  ClaimContext,
  PremiumCalculator,
  PremiumCalculationStrategy,
  StandardPremiumStrategy,
  PremiumPlusStrategy,
  DiscountedPremiumStrategy
};
