# InsureFlow — Walkthrough

## What Was Built

A complete MVC Insurance Workflow Automation app with **Node.js + Express + EJS + SQLite**.

### MVC Architecture Implemented

| Layer | Files |
|---|---|
| **Model** | [User.js](file:///home/harsh-tandon/ooad_project/models/User.js), [Policy.js](file:///home/harsh-tandon/ooad_project/models/Policy.js), [Quote.js](file:///home/harsh-tandon/ooad_project/models/Quote.js), [Payment.js](file:///home/harsh-tandon/ooad_project/models/Payment.js), [Claim.js](file:///home/harsh-tandon/ooad_project/models/Claim.js) |
| **Controller** | [authController.js](file:///home/harsh-tandon/ooad_project/controllers/authController.js), [policyController.js](file:///home/harsh-tandon/ooad_project/controllers/policyController.js), [purchaseController.js](file:///home/harsh-tandon/ooad_project/controllers/purchaseController.js), [claimController.js](file:///home/harsh-tandon/ooad_project/controllers/claimController.js) |
| **View** | 13 EJS templates (login, register, dashboard, policies, quote, payment, claims, admin pages) |
| **Database** | SQLite with 5 tables (users, policies, quotes, payments, claims) |
| **Middleware** | Auth (session check) + RBAC (role-based access) |

---

## Browser Test Results ✅

All flows tested and verified:

````carousel
![Dashboard — role-based card navigation](/home/harsh-tandon/.gemini/antigravity/brain/ca6ed2bb-fb98-40cf-8116-06cba4abbfc2/dashboard.png)
<!-- slide -->
![Policy search with filter and results](/home/harsh-tandon/.gemini/antigravity/brain/ca6ed2bb-fb98-40cf-8116-06cba4abbfc2/policies.png)
<!-- slide -->
![Purchase success confirmation](/home/harsh-tandon/.gemini/antigravity/brain/ca6ed2bb-fb98-40cf-8116-06cba4abbfc2/purchase_success.png)
````

### Full demo recording:
![Full app test — login, purchase, claims, admin](/home/harsh-tandon/.gemini/antigravity/brain/ca6ed2bb-fb98-40cf-8116-06cba4abbfc2/insureflow_full_test_1774966689895.webp)

---

## Verified Flows

| Flow | Status |
|---|---|
| Register & Login | ✅ |
| Dashboard (role-based) | ✅ |
| Policy Search & Filter | ✅ |
| Quote Generation | ✅ |
| Payment & Policy Issuance | ✅ |
| My Policies listing | ✅ |
| Claim Submission | ✅ |
| Claim Tracking | ✅ |
| Admin: Manage Policies | ✅ |
| Admin/Adjuster: Review Claims | ✅ |
| RBAC (role-based access) | ✅ |
| Logout | ✅ |

---

## How to Run

```bash
cd ~/ooad_project
node seed.js    # seed demo data
node app.js     # start server on port 3000
```

**Demo logins:** `harsh@example.com` / `password123` (customer), `admin@example.com` / `admin123` (admin)
