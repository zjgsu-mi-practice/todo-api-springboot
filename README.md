# Todo API

A RESTful API for managing todo items, built with Spring Boot and Kotlin.

## Features

- Create, read, update, and delete todos
- Categorize todos by categories
- Tag todos with multiple tags
- Add memos with attachments to todos
- Set reminders with different notification methods

## Tech Stack

- Kotlin 1.9.25
- Spring Boot 3.4.4
- Spring Data JPA
- H2 Database (for development)
- Gradle

## Getting Started

### Prerequisites

- JDK 17 or higher
- Gradle (or use the included Gradle wrapper)

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Gradle:

```bash
./gradlew bootRun
```

The API will be available at http://localhost:8080

### H2 Console

You can access the H2 database console at http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:mem:tododb`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Todos

- `GET /api/todos` - Get all todos
- `GET /api/todos?status=pending` - Get todos by status
- `GET /api/todos/{id}` - Get todo by ID
- `POST /api/todos` - Create new todo
- `PUT /api/todos/{id}` - Update todo
- `DELETE /api/todos/{id}` - Delete todo

### Categories

- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### Tags

- `GET /api/tags` - Get all tags
- `GET /api/tags/{id}` - Get tag by ID
- `POST /api/tags` - Create new tag
- `PUT /api/tags/{id}` - Update tag
- `DELETE /api/tags/{id}` - Delete tag

### Memos

- `GET /api/memos/{id}` - Get memo by ID
- `POST /api/memos` - Create new memo
- `PUT /api/memos/{id}` - Update memo
- `DELETE /api/memos/{id}` - Delete memo

### Reminders

- `GET /api/reminders/{id}` - Get reminder by ID
- `GET /api/reminders/todo/{todoId}` - Get reminders by todo ID
- `POST /api/reminders` - Create new reminder
- `PUT /api/reminders/{id}` - Update reminder
- `DELETE /api/reminders/{id}` - Delete reminder

## Sample Requests

### Create a Todo

```json
POST /api/todos
{
  "title": "Complete project",
  "description": "Finish the Spring Boot project",
  "dueDate": "2023-12-31T12:00:00Z",
  "categoryId": "category-uuid",
  "tagIds": ["tag1-uuid", "tag2-uuid"]
}
```

### Update a Todo

```json
PUT /api/todos/{id}
{
  "title": "Complete project (updated)",
  "status": "in_progress"
}
```

## Development

The application runs with an in-memory H2 database and is pre-loaded with sample data when running with the `dev` profile. 

## Testing

[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen.svg)](https://github.com/zjgsu-mi-practice/todo-api-springboot)

This project includes comprehensive test coverage with:

- Unit tests for controllers and services
- Integration tests for API endpoints

See [TESTING.md](TESTING.md) for complete testing documentation.

### Run Tests

To run the tests:

```bash
./gradlew test
```