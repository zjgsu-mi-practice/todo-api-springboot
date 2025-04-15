# Testing Guide

This document outlines the testing approach for the Todo API application.

## Test Structure

The application's test suite is organized into several categories:

### Unit Tests

These tests focus on testing individual components in isolation:

- **Controller Tests**: Verify that controllers handle HTTP requests correctly and return appropriate responses.
  - Located in: `src/test/kotlin/com/zjgsu/todo/controller/`
  - Examples: `TodoControllerTest.kt`, `CategoryControllerTest.kt`

- **Service Tests**: Ensure that business logic is correctly implemented.
  - Located in: `src/test/kotlin/com/zjgsu/todo/service/`
  - Examples: `TodoServiceTest.kt`, `ReminderServiceTest.kt`

### Integration Tests

These tests verify that different components work correctly together:

- **API Integration Tests**: Test the complete request-response cycle through the API.
  - Located in: `src/test/kotlin/com/zjgsu/todo/integration/`
  - Example: `TodoIntegrationTest.kt`

## Test Technology Stack

- **JUnit 5**: The main testing framework
- **MockK**: Kotlin mocking library for creating test doubles
- **Spring Boot Test**: Utilities for testing Spring Boot applications
- **Kotest**: Used for additional testing capabilities

## Running Tests

### Using Gradle

```bash
./gradlew test
```

### Using Makefile

```bash
make test
```

### Using Shell Script

```bash
./run-tests.sh
```

## CI/CD Integration

Tests are automatically run in CI/CD pipelines using GitHub Actions. See the workflow configuration at `.github/workflows/test.yml`.

## Best Practices

When adding new features or modifying existing code, follow these testing practices:

1. **Write Tests First**: Consider a test-driven development approach, writing tests before implementing features.
2. **Test Edge Cases**: Include tests for boundary conditions and error scenarios.
3. **Isolation**: Unit tests should focus on testing components in isolation using mocks for dependencies.
4. **Readability**: Use descriptive test names that explain the scenario being tested.
5. **Coverage**: Aim for high test coverage of all new code.

## Adding New Tests

When adding new features, follow this testing checklist:

1. Create unit tests for new controllers and services
2. Add integration tests for API endpoints if necessary
3. Verify both happy path and error scenarios
4. Run the full test suite before committing changes 