/**
 * STATE PATTERN (Behavioral)
 * Created by: Dishan
 * 
 * Purpose: Allows an object to alter its behavior when its internal state changes.
 * Used to manage the lifecycle of Policies and Claims through different states.
 * 
 * Benefits:
 * - Encapsulates state-specific behavior
 * - Eliminates complex conditional logic for state transitions
 * - Easy to add new states
 * - Each state is independently testable
 * - Clear state machine visualization
 */

/**
 * State - Base class for all states
 */
class State {
  /**
   * Handle state transitions and operations
   */
  onEnter(context) {
    // Override in subclasses
  }

  canTransitionTo(nextState) {
    return false;
  }

  getName() {
    throw new Error('getName() must be implemented');
  }
}

// ==================== POLICY STATES ====================

/**
 * PolicyState - Base class for policy states
 */
class PolicyState extends State {
  /**
   * Renew the policy
   */
  renew(context) {
    throw new Error(`Cannot renew policy in ${this.getName()} state`);
  }

  /**
   * Cancel the policy
   */
  cancel(context) {
    throw new Error(`Cannot cancel policy in ${this.getName()} state`);
  }

  /**
   * Suspend the policy
   */
  suspend(context) {
    throw new Error(`Cannot suspend policy in ${this.getName()} state`);
  }

  /**
   * File a claim
   */
  fileClaim(context) {
    throw new Error(`Cannot file claim in ${this.getName()} state`);
  }
}

/**
 * ActivePolicyState - Policy is active and valid
 */
class ActivePolicyState extends PolicyState {
  getName() {
    return 'active';
  }

  canTransitionTo(nextState) {
    return nextState instanceof ExpiredPolicyState || 
           nextState instanceof CancelledPolicyState ||
           nextState instanceof SuspendedPolicyState;
  }

  renew(context) {
    console.log(`Renewing policy ${context.policyId}`);
    context.setState(new ActivePolicyState());
    return true;
  }

  cancel(context) {
    console.log(`Cancelling policy ${context.policyId}`);
    context.setState(new CancelledPolicyState());
    return true;
  }

  suspend(context) {
    console.log(`Suspending policy ${context.policyId}`);
    context.setState(new SuspendedPolicyState());
    return true;
  }

  fileClaim(context) {
    console.log(`Filing claim for policy ${context.policyId}`);
    return true;
  }
}

/**
 * ExpiredPolicyState - Policy has expired
 */
class ExpiredPolicyState extends PolicyState {
  getName() {
    return 'expired';
  }

  canTransitionTo(nextState) {
    return nextState instanceof ActivePolicyState;
  }

  renew(context) {
    console.log(`Renewing expired policy ${context.policyId}`);
    context.setState(new ActivePolicyState());
    return true;
  }

  fileClaim(context) {
    throw new Error(`Cannot file claim on expired policy ${context.policyId}`);
  }
}

/**
 * CancelledPolicyState - Policy has been cancelled
 */
class CancelledPolicyState extends PolicyState {
  getName() {
    return 'cancelled';
  }

  canTransitionTo(nextState) {
    return false; // Cannot transition from cancelled state
  }

  fileClaim(context) {
    throw new Error(`Cannot file claim on cancelled policy ${context.policyId}`);
  }
}

/**
 * SuspendedPolicyState - Policy is temporarily suspended
 */
class SuspendedPolicyState extends PolicyState {
  getName() {
    return 'suspended';
  }

  canTransitionTo(nextState) {
    return nextState instanceof ActivePolicyState || 
           nextState instanceof CancelledPolicyState;
  }

  cancel(context) {
    console.log(`Cancelling suspended policy ${context.policyId}`);
    context.setState(new CancelledPolicyState());
    return true;
  }

  fileClaim(context) {
    throw new Error(`Cannot file claim on suspended policy ${context.policyId}`);
  }
}

// ==================== CLAIM STATES ====================

/**
 * ClaimState - Base class for claim states
 */
class ClaimState extends State {
  /**
   * Approve the claim
   */
  approve(context) {
    throw new Error(`Cannot approve claim in ${this.getName()} state`);
  }

  /**
   * Reject the claim
   */
  reject(context, reason) {
    throw new Error(`Cannot reject claim in ${this.getName()} state`);
  }

  /**
   * Start review of the claim
   */
  startReview(context) {
    throw new Error(`Cannot start review in ${this.getName()} state`);
  }

  /**
   * Payout the claim
   */
  payout(context) {
    throw new Error(`Cannot payout claim in ${this.getName()} state`);
  }
}

/**
 * SubmittedClaimState - Claim has been submitted
 */
class SubmittedClaimState extends ClaimState {
  getName() {
    return 'submitted';
  }

  canTransitionTo(nextState) {
    return nextState instanceof UnderReviewClaimState;
  }

  startReview(context) {
    console.log(`Starting review for claim ${context.claimId}`);
    context.setState(new UnderReviewClaimState());
    return true;
  }
}

/**
 * UnderReviewClaimState - Claim is being reviewed
 */
class UnderReviewClaimState extends ClaimState {
  getName() {
    return 'under_review';
  }

  canTransitionTo(nextState) {
    return nextState instanceof ApprovedClaimState || 
           nextState instanceof RejectedClaimState;
  }

  approve(context) {
    console.log(`Approving claim ${context.claimId}`);
    context.setState(new ApprovedClaimState());
    return true;
  }

  reject(context, reason) {
    console.log(`Rejecting claim ${context.claimId}. Reason: ${reason}`);
    context.setState(new RejectedClaimState());
    context.rejectionReason = reason;
    return true;
  }
}

/**
 * ApprovedClaimState - Claim has been approved
 */
class ApprovedClaimState extends ClaimState {
  getName() {
    return 'approved';
  }

  canTransitionTo(nextState) {
    return nextState instanceof PaidClaimState;
  }

  payout(context) {
    console.log(`Processing payout for claim ${context.claimId}`);
    context.setState(new PaidClaimState());
    return true;
  }
}

/**
 * RejectedClaimState - Claim has been rejected
 */
class RejectedClaimState extends ClaimState {
  getName() {
    return 'rejected';
  }

  canTransitionTo(nextState) {
    return false; // Cannot transition from rejected state
  }
}

/**
 * PaidClaimState - Claim has been paid out
 */
class PaidClaimState extends ClaimState {
  getName() {
    return 'paid';
  }

  canTransitionTo(nextState) {
    return false; // Cannot transition from paid state
  }
}

// ==================== CONTEXT CLASSES ====================

/**
 * PolicyContext - Manages policy state
 */
class PolicyContext {
  constructor(policyId, initialState = 'active') {
    this.policyId = policyId;
    this.state = this.createState(initialState);
    this.state.onEnter(this);
  }

  createState(stateName) {
    const states = {
      'active': new ActivePolicyState(),
      'expired': new ExpiredPolicyState(),
      'cancelled': new CancelledPolicyState(),
      'suspended': new SuspendedPolicyState()
    };

    return states[stateName] || new ActivePolicyState();
  }

  setState(newState) {
    if (!this.state.canTransitionTo(newState)) {
      throw new Error(
        `Cannot transition from ${this.state.getName()} to ${newState.getName()}`
      );
    }
    this.state = newState;
    this.state.onEnter(this);
  }

  getState() {
    return this.state.getName();
  }

  // Delegate operations to state
  renew() {
    return this.state.renew(this);
  }

  cancel() {
    return this.state.cancel(this);
  }

  suspend() {
    return this.state.suspend(this);
  }

  fileClaim() {
    return this.state.fileClaim(this);
  }
}

/**
 * ClaimContext - Manages claim state
 */
class ClaimContext {
  constructor(claimId, initialState = 'submitted') {
    this.claimId = claimId;
    this.state = this.createState(initialState);
    this.rejectionReason = null;
    this.state.onEnter(this);
  }

  createState(stateName) {
    const states = {
      'submitted': new SubmittedClaimState(),
      'under_review': new UnderReviewClaimState(),
      'approved': new ApprovedClaimState(),
      'rejected': new RejectedClaimState(),
      'paid': new PaidClaimState()
    };

    return states[stateName] || new SubmittedClaimState();
  }

  setState(newState) {
    if (!this.state.canTransitionTo(newState)) {
      throw new Error(
        `Cannot transition from ${this.state.getName()} to ${newState.getName()}`
      );
    }
    this.state = newState;
    this.state.onEnter(this);
  }

  getState() {
    return this.state.getName();
  }

  // Delegate operations to state
  startReview() {
    return this.state.startReview(this);
  }

  approve() {
    return this.state.approve(this);
  }

  reject(reason) {
    return this.state.reject(this, reason);
  }

  payout() {
    return this.state.payout(this);
  }
}

module.exports = {
  // Policy States
  PolicyState,
  ActivePolicyState,
  ExpiredPolicyState,
  CancelledPolicyState,
  SuspendedPolicyState,
  PolicyContext,

  // Claim States
  ClaimState,
  SubmittedClaimState,
  UnderReviewClaimState,
  ApprovedClaimState,
  RejectedClaimState,
  PaidClaimState,
  ClaimContext
};
