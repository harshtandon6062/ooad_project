# InsureFlow — Insurance Workflow Automation Software

OOAD Course Project — MVC Architecture in Java Spring Boot

## Quick Start

```bash
# Install dependencies
mvn install

# Run the application
mvn spring-boot:run
```

Open http://localhost:8080

## Architecture (MVC)

```
User (UI — Thymeleaf templates)
   ↓
Controller (Spring MVC controllers)
   ↓
Service (Business logic + Design Patterns)
   ↓
Repository (JPA repositories)
   ↓
Database (H2)
```

## Tech Stack

- **Backend:** Java + Spring Boot
- **View:** Thymeleaf
- **Database:** H2 (in-memory)
- **Build:** Maven

---

## Design Patterns Implementation

This project implements **4 core design patterns** covering Creational, Structural, and Behavioral categories.

### 1. Factory Pattern (Creational)

**Location:** `patterns/creational/`

**Purpose:** Encapsulates the creation of complex domain objects.

**Classes:**
- `PolicyFactory`: Creates policy instances
- `ClaimFactory`: Creates claim instances
- `QuoteFactory`: Creates quotation instances

### 2. Strategy Pattern (Behavioral)

**Location:** `patterns/behavioral/`

**Purpose:** Defines different premium calculation algorithms.

**Classes:**
- `PremiumStrategy` (interface)
- `StandardPremiumStrategy`
- `PremiumPlusStrategy`
- `DiscountedPremiumStrategy`
- `PremiumCalculator` (context)

### 3. State Pattern (Behavioral)

**Location:** `patterns/behavioral/`

**Purpose:** Manages lifecycle of Policies and Claims.

**Classes:**
- `PolicyState`, `PolicyContext`
- `ClaimState`, `ClaimContext`
- Concrete states: `ActivePolicyState`, `ExpiredPolicyState`, etc.

### 4. Facade Pattern (Structural)

**Location:** `patterns/structural/InsuranceFacade.java`

**Purpose:** Provides unified interface to the insurance subsystem.

---

## Files Structure

```
patterns/
├── creational/
│   ├── PolicyFactory.java
│   ├── ClaimFactory.java
│   └── QuoteFactory.java
├── structural/
│   └── InsuranceFacade.java
└── behavioral/
    ├── strategy/
    │   ├── PremiumStrategy.java
    │   ├── StandardPremiumStrategy.java
    │   ├── PremiumPlusStrategy.java
    │   ├── DiscountedPremiumStrategy.java
    │   └── PremiumCalculator.java
    └── state/
        ├── PolicyState.java
        ├── ActivePolicyState.java
        ├── ExpiredPolicyState.java
        ├── CancelledPolicyState.java
        ├── SuspendedPolicyState.java
        ├── PolicyContext.java
        ├── ClaimState.java
        ├── SubmittedClaimState.java
        ├── UnderReviewClaimState.java
        ├── ApprovedClaimState.java
        ├── RejectedClaimState.java
        ├── PaidClaimState.java
        └── ClaimContext.java
```