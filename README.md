# InsureFlow — Insurance Workflow Automation Software

OOAD Course Project — MVC Architecture

## Quick Start

```bash
# Install dependencies
npm install

# Seed demo data
node seed.js

# Start the server
node app.js
```

Open http://localhost:3000

## Demo Credentials

| Role | Email | Password |
|---|---|---|
| Customer | harsh@example.com | password123 |
| Agent | priya@example.com | password123 |
| Admin | admin@example.com | admin123 |
| Claims Adjuster | raj@example.com | password123 |

## Architecture (MVC)

```
User (UI — EJS templates)
   ↓
Controller (Express route handlers)
   ↓
Model (business logic + SQLite)
   ↓
Database (SQLite)
```

## Tech Stack

- **Backend:** Node.js + Express.js
- **View:** EJS + Bootstrap 5
- **Database:** SQLite (better-sqlite3)
- **Auth:** express-session + bcryptjs

---

## Design Patterns Implementation

This project implements **4 core design patterns** covering Creational, Structural, and Behavioral categories. Each pattern is assigned to a team member and solves specific problems in the insurance domain.

### 1. Factory Pattern (Creational) — Suman

**Location:** `patterns/creational/factory.js`

**Purpose:** Encapsulates the creation of complex domain objects (Policy, Claim, Quote) with centralized validation logic.

**Classes:**
- `PolicyFactory`: Creates and manages policy instances
  - `createTemplate(type, provider, premium, coverageAmount)` — Create policy template
  - `issuePolicy(userId, type, provider, ...)` — Issue policy to user
  - `getAvailablePolicies(filters)` — Get template policies
  - `getUserPolicies(userId)` — Get user's active policies

- `ClaimFactory`: Creates and manages claim instances
  - `submitClaim(policyId, userId, amount, description)` — Submit new claim
  - `getUserClaims(userId)` — Get user's claims
  - `getAllClaims()` — Get all claims (admin)
  - `updateClaimStatus(claimId, status)` — Update claim status

- `QuoteFactory`: Creates and manages quotations
  - `createQuote(userId, policyId, premium, validUntil)` — Generate quote
  - `getUserQuotes(userId)` — Get user's quotes
  - `getQuote(quoteId)` — Get specific quote

**Benefits:**
- ✅ Centralized object creation logic with validation
- ✅ Consistent handling across all object types
- ✅ Easy to extend with new policy/claim types
- ✅ Decouples creation from usage
- ✅ Validation happens at creation time

**Example Usage:**
```javascript
const { PolicyFactory } = require('./patterns/creational/factory');

// Create a policy template
const policyId = PolicyFactory.createTemplate('health', 'InsureCo', 5000, 500000);

// Issue policy to user
const issuedId = PolicyFactory.issuePolicy(userId, 'health', 'InsureCo', 5000, 500000, startDate, expiryDate);

// Get available policies with filters
const policies = PolicyFactory.getAvailablePolicies({ type: 'health', maxPremium: 10000 });
```

---

### 2. Strategy Pattern (Behavioral) — Dhruv

**Location:** `patterns/behavioral/strategy.js`

**Purpose:** Defines different premium calculation algorithms and makes them interchangeable at runtime.

**Strategy Classes:**
- `StandardPremiumStrategy`: Base premium with age factor adjustment
  - 1.5x for age < 25
  - 1.0x for age 25-50
  - 1.3x for age 50-65
  - 1.6x for age 65+

- `PremiumPlusStrategy`: Premium with lifestyle and occupation factors
  - Includes age, lifestyle (smoker, pre-existing conditions), occupation risk
  - More comprehensive risk assessment

- `DiscountedPremiumStrategy`: Premium with loyalty and claims discounts
  - Multi-policy discount (10%)
  - Loyalty discount for 1+ year members (15%)
  - No-claims discount for 2+ years (20%)

**Context Class:**
- `PremiumCalculator`: Uses strategies to calculate premiums
  - `setStrategy(strategy)` — Switch calculation strategy
  - `calculatePremium(policy, userData)` — Calculate premium using current strategy

**Benefits:**
- ✅ Easy to add new premium calculation methods
- ✅ Eliminates complex conditional logic in controllers
- ✅ Strategies can be changed at runtime
- ✅ Each strategy is independently testable
- ✅ Follows Open/Closed principle

**Example Usage:**
```javascript
const { PremiumCalculator, StandardPremiumStrategy, PremiumPlusStrategy } = require('./patterns/behavioral/strategy');

const calculator = new PremiumCalculator(new StandardPremiumStrategy());
const premium = calculator.calculatePremium(policy, { age: 30 });

// Switch strategy
calculator.setStrategy(new PremiumPlusStrategy());
const premiumPlus = calculator.calculatePremium(policy, { age: 30, smoker: true, occupation: 'construction' });
```

---

### 3. State Pattern (Behavioral) — Dishan

**Location:** `patterns/behavioral/state.js`

**Purpose:** Manages the lifecycle and state transitions of Policies and Claims, encapsulating state-specific behavior.

**Policy States:**
- `ActivePolicyState`: Policy is active and valid
  - Can: renew, cancel, suspend, file claim
- `ExpiredPolicyState`: Policy has expired
  - Can: renew
  - Cannot: file claim
- `CancelledPolicyState`: Policy has been cancelled
  - Cannot: file claim, renew
  - No transitions allowed
- `SuspendedPolicyState`: Policy is temporarily suspended
  - Can: cancel, reactivate
  - Cannot: file claim

**Claim States:**
- `SubmittedClaimState`: Initial state when claim is submitted
  - Can: start review
- `UnderReviewClaimState`: Claim is being reviewed
  - Can: approve or reject
- `ApprovedClaimState`: Claim has been approved
  - Can: process payout
- `RejectedClaimState`: Claim has been rejected
  - No transitions
- `PaidClaimState`: Claim has been paid
  - No transitions

**Context Classes:**
- `PolicyContext`: Manages policy state and transitions
  - `setState(newState)` — Transition to new state
  - `renew()`, `cancel()`, `suspend()`, `fileClaim()` — Delegate to state

- `ClaimContext`: Manages claim state and transitions
  - `setState(newState)` — Transition to new state
  - `startReview()`, `approve()`, `reject()`, `payout()` — Delegate to state

**Benefits:**
- ✅ Eliminates complex conditional logic for state management
- ✅ Encapsulates state-specific behavior
- ✅ Validates legal state transitions
- ✅ Easy to add new states
- ✅ Clear state machine visualization

**Example Usage:**
```javascript
const { PolicyContext, ClaimContext } = require('./patterns/behavioral/state');

// Manage policy lifecycle
const policy = new PolicyContext('POL-123', 'active');
policy.suspend();      // ActivePolicyState → SuspendedPolicyState
policy.cancel();       // SuspendedPolicyState → CancelledPolicyState

// Manage claim lifecycle
const claim = new ClaimContext('CLM-456', 'submitted');
claim.startReview();   // SubmittedClaimState → UnderReviewClaimState
claim.approve();       // UnderReviewClaimState → ApprovedClaimState
claim.payout();        // ApprovedClaimState → PaidClaimState
```

---

### 4. Facade Pattern (Structural) — Harsh

**Location:** `patterns/structural/facade.js`

**Purpose:** Provides a unified, simplified interface to the complex insurance subsystem. Hides interactions between controllers, models, factories, and strategies.

**Main Class:**
- `InsuranceFacade`: Static facade class with 15+ public methods

**User Operations:**
- `registerUser(name, email, password, role)` — Register new user
- `authenticateUser(email, password)` — Authenticate user
- `getUserProfile(userId)` — Get user profile

**Policy Operations:**
- `searchAvailablePolicies(filters)` — Search policies with filters
- `getPolicyDetails(policyId)` — Get full policy details
- `getUserPolicies(userId)` — Get user's purchased policies
- `createPolicyTemplate(type, provider, premium, coverage)` — Create template (admin)

**Quote Operations:**
- `generateQuote(userId, policyId, userData)` — Generate quote with premium calculation

**Purchase Operations:**
- `processPurchase(userId, quoteId, paymentMethod)` — Process purchase and payment

**Claim Operations:**
- `submitClaim(userId, policyId, amount, description)` — Submit claim
- `getUserClaims(userId)` — Get user's claims
- `getAllClaims()` — Get all claims (admin)
- `updateClaimStatus(claimId, status)` — Update claim status with state validation
- `getClaimDetails(claimId)` — Get claim details

**Dashboard:**
- `getUserDashboard(userId)` — Get dashboard statistics

**Benefits:**
- ✅ Simplifies controller code (no need to interact with multiple models)
- ✅ Centralizes business logic
- ✅ Improves maintainability and readability
- ✅ Reduces coupling between layers
- ✅ Single point of contact for complex operations
- ✅ Integrates Factory, Strategy, and State patterns seamlessly

**Example Usage:**
```javascript
const InsuranceFacade = require('./patterns/structural/facade');

// User registration
const result = InsuranceFacade.registerUser('John Doe', 'john@example.com', 'password123');

// Search and purchase
const policies = InsuranceFacade.searchAvailablePolicies({ type: 'health' });
const quote = InsuranceFacade.generateQuote(userId, policyId, { age: 30 });
const purchase = InsuranceFacade.processPurchase(userId, quoteId, 'card');

// Claim management
const claim = InsuranceFacade.submitClaim(userId, policyId, 50000, 'Medical emergency');
const claimUpdate = InsuranceFacade.updateClaimStatus(claimId, 'approved');
```

---

## Pattern Integration Flow

```
                    User Request (Controller)
                           ↓
                   InsuranceFacade (Facade)
                    ↙          ↓         ↘
            ┌─────────────┬───────────┬──────────────┐
            ↓             ↓           ↓              ↓
        Factory      Strategy      State          Models
       Pattern       Pattern       Pattern       (DAOs)
     (Creation)   (Calculation)  (Lifecycle)
     ┌───────┐     ┌────────┐     ┌──────┐
     │ Policy│     │Standard│     │Active │
     │Claim  │     │Premium │     │Expired│
     │Quote  │     │Plus    │     │Cancel │
     └───────┘     │Discount│     │Suspend│
                   └────────┘     └──────┘
```

---

## Using the Patterns in Controllers

Instead of this (without patterns):
```javascript
// Old: Multiple imports, scattered logic
const Policy = require('../models/Policy');
const Quote = require('../models/Quote');
const Payment = require('../models/Payment');

router.post('/policies/:id/quote', async (req, res) => {
  const policy = Policy.findById(req.params.id);
  const ageFactor = req.body.age > 40 ? 1.3 : 1.0;  // Hard-coded logic
  const premium = policy.premium * ageFactor;
  const quoteId = Quote.create(req.session.user.user_id, policy.policy_id, premium, validUntil);
  // ... more logic
});
```

Use this (with patterns):
```javascript
// New: Single facade call, clean and simple
const InsuranceFacade = require('../patterns/structural/facade');

router.post('/policies/:id/quote', async (req, res) => {
  const result = InsuranceFacade.generateQuote(
    req.session.user.user_id,
    req.params.id,
    { age: req.body.age }
  );
  
  if (result.success) {
    res.render('quote', { quote: result.quote, premium: result.premiumEstimate });
  } else {
    res.render('error', { message: result.message });
  }
});
```

---

## Files Structure

```
patterns/
├── index.js                    # Main exports
├── creational/
│   └── factory.js             # Factory Pattern (Suman)
├── structural/
│   └── facade.js              # Facade Pattern (Harsh)
└── behavioral/
    ├── strategy.js            # Strategy Pattern (Dhruv)
    └── state.js               # State Pattern (Dishan)
```
