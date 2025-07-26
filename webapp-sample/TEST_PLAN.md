# Test Strategy (React + Node API)

## Scope
- **UI (Selenium/Java)**: login, create, edit, delete todo items and data assertions on the React app.
- **API (Cucumber + REST-Assured/Java)**: POST `/login`, GET `/items`, POST `/items`, PUT `/items/:id`, DELETE `/items/:id` with positive + negative cases.

## Coverage
- Auth: valid/invalid login, unauthorized GET `/items`.
- Items CRUD: happy path and failures (missing fields, non-existent ids).
- UI flows: invalid login shows error; valid login renders todos; create/edit/delete reflects server state.

## Tools
- **Selenium 4 + WebDriverManager + JUnit 5** for reliable, headless UI tests.
- **Cucumber + REST-Assured** for readable API scenarios and JSON-driven payloads.
- **Vite + React** minimal front-end.
- **Express** in-memory API—fast and deterministic.

## How to Run
1. **Install deps** (or use the setup script you paste in your CI):
   - Node 18+, Java 17, Maven 3.9+, Google Chrome.
2. **Start the services** in two terminals:
   - API: `npm --prefix server ci && npm --prefix server start`
   - UI: `npm --prefix client ci && npm --prefix client run dev`
3. **Run API tests**:
   - `mvn -q -f tests/api/pom.xml test` (override base URL: `-DbaseUrl=http://localhost:4000`)
4. **Run UI tests**:
   - `mvn -q -f tests/ui/pom.xml test -DbaseUrl=http://localhost:5173`

## Assumptions / Limitations
- In-memory API store; state resets per server run.
- Simple token scheme: fixed token for demo (`fake-token-123`).
- Headless Chrome is used; timing-sensitive UI waits are minimized by keeping UI simple.
