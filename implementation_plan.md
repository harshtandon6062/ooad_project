# Insurance Workflow Automation — Implementation Plan

## Tech Stack

| Layer | Choice |
|---|---|
| **Backend** | Node.js + Express.js |
| **View (UI)** | EJS templates + Bootstrap 5 (CDN) |
| **Database** | SQLite (better-sqlite3) |
| **Auth** | express-session + bcrypt |

---

## MVC Mapping to Files

### View Layer — 7 Pages (EJS templates in `views/`)

| Page | File | Purpose |
|---|---|---|
| Login Page | `login.ejs` | Login + Register |
| Dashboard | `dashboard.ejs` | Role-based home |
| Policy Search | `policies.ejs` | Browse/search policies |
| Quote Display | `quote.ejs` | Show generated quote |
| Payment Page | `payment.ejs` | Simulated payment |
| Claim Submission | `claim-submit.ejs` | Submit a claim |
| Claim Status | `claims.ejs` | Track claim status |

### Controller Layer — 4 Controllers (`controllers/`)

| Controller | Methods |
|---|---|
| **AuthController** | `login()`, `logout()`, `register()` |
| **PolicyController** | `searchPolicy()`, `generateQuote()`, `getPolicyDetails()` |
| **PurchaseController** | `createOrder()`, `processPayment()`, `issuePolicy()` |
| **ClaimController** | `submitClaim()`, `trackClaim()`, `updateClaimStatus()` |

### Model Layer — 5 Models (`models/`)

| Model | Key Fields |
|---|---|
| **User** | userId, name, email, password, role |
| **Policy** | policyId, type, premium, startDate, expiryDate |
| **Quote** | quoteId, premiumEstimate, validUntil |
| **Payment** | paymentId, amount, status |
| **Claim** | claimId, status, amount |

### Database — 5 SQL Tables
`users`, `policies`, `quotes`, `payments`, `claims` — exactly as you specified.

---

## Folder Structure

```
ooad_project/
├── models/
│   ├── User.js, Policy.js, Quote.js, Payment.js, Claim.js
├── controllers/
│   ├── authController.js, policyController.js
│   ├── purchaseController.js, claimController.js
├── routes/
│   ├── authRoutes.js, policyRoutes.js
│   ├── purchaseRoutes.js, claimRoutes.js
├── views/
│   ├── partials/  (header.ejs, footer.ejs)
│   ├── login.ejs, register.ejs, dashboard.ejs
│   ├── policies.ejs, quote.ejs, payment.ejs
│   ├── claim-submit.ejs, claims.ejs, claim-review.ejs
├── middleware/
│   └── auth.js
├── public/css/
│   └── style.css
├── config/
│   └── db.js
├── seed.js
├── app.js
├── package.json
└── README.md
```

---

## Build Order

1. **DB + Models** — `config/db.js` creates tables, model files wrap SQL queries
2. **Auth** — register/login/logout + session middleware
3. **Policy + Purchase** — search, quote, payment, issue
4. **Claims** — submit, track, review
5. **Views** — all EJS pages with Bootstrap styling
6. **Seed data** — demo users and policies

---

## Verification

1. `node seed.js` → populate sample data
2. `node app.js` → open `http://localhost:3000`
3. Test full flow: Register → Login → Search → Quote → Pay → Dashboard → Claim → Track
