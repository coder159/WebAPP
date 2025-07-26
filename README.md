# WebAPP Demo

This repository contains a small full-stack example used for API and UI test demonstrations. It has a simple Node.js API server, a React front-end and automated tests written in Java.

## Project Structure

```
webapp-sample/
  server/     # Express API serving /login and /items
  client/     # React todo app served by Vite
  tests/      # Java test suites for the API and UI
    api/      # Cucumber + REST-Assured tests
    ui/       # Selenium / JUnit tests
 tests/
   api/src/test/resources/features/   # Active Cucumber feature files
```

## Running the Application

1. Start the API:
   ```bash
   cd webapp-sample/server
   npm install
   npm start      # http://localhost:4000
   ```

2. Start the React front-end in another terminal:
   ```bash
   cd webapp-sample/client
   npm install
   npm run dev    # http://localhost:5173
   ```

The API keeps data in memory so the todo list resets whenever the server restarts.

## Automated Tests

Java 17 and Maven are required to run the tests. The provided `SETUP_SCRIPT.sh` installs the necessary dependencies including Chrome for headless UI tests.

### API Tests

```
cd webapp-sample/tests/api
mvn test -DbaseUrl=http://localhost:4000
```

The feature file used by these tests is located under `tests/api/src/test/resources/features`.

### UI Tests

```
cd webapp-sample/tests/ui
mvn test -DbaseUrl=http://localhost:5173
```

The UI tests use Selenium with headless Chrome. Ensure the API and UI servers are running before executing them.

## Additional Resources

- **SETUP_SCRIPT.sh** – installs Node, Java, Maven and Chrome and pre-builds the Maven projects.
- **TEST_PLAN.md** – outlines the intended test coverage for the API and UI.

