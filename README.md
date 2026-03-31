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
