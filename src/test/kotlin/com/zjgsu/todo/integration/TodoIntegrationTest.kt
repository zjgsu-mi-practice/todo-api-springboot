package com.zjgsu.todo.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.zjgsu.todo.dto.CreateTodoRequest
import com.zjgsu.todo.dto.TodoResponse
import com.zjgsu.todo.dto.UpdateTodoRequest
import com.zjgsu.todo.model.Todo
import com.zjgsu.todo.model.TodoStatus
import com.zjgsu.todo.repository.TodoRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.OffsetDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class TodoIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @AfterEach
    fun cleanup() {
        todoRepository.deleteAll()
    }

    @Test
    fun `should create and retrieve a todo`() {
        // Given
        val createRequest = CreateTodoRequest(
            title = "Integration Test Todo",
            description = "Testing the full API flow",
            dueDate = OffsetDateTime.now()
        )

        // When - Create todo
        val createResult = mockMvc.perform(
            post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("Integration Test Todo"))
            .andReturn()

        // Extract the created todo ID
        val createdTodo = objectMapper.readValue(
            createResult.response.contentAsString,
            TodoResponse::class.java
        )
        val todoId = createdTodo.id

        // Then - Retrieve the todo
        mockMvc.perform(get("/api/todos/$todoId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(todoId.toString()))
            .andExpect(jsonPath("$.title").value("Integration Test Todo"))
            .andExpect(jsonPath("$.status").value("pending"))
    }

    @Test
    fun `should update a todo`() {
        // Given - Create a todo first
        val todo = Todo(
            title = "Original Title",
            description = "Original Description",
            status = TodoStatus.PENDING
        )
        val savedTodo = todoRepository.save(todo)

        val updateRequest = UpdateTodoRequest(
            title = "Updated Integration Title",
            status = "completed"
        )

        // When - Update the todo
        mockMvc.perform(
            put("/api/todos/${savedTodo.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Updated Integration Title"))
            .andExpect(jsonPath("$.status").value("completed"))

        // Then - Verify the update in the database
        val updatedTodo = todoRepository.findById(savedTodo.id!!).get()
        assertEquals("Updated Integration Title", updatedTodo.title)
        assertEquals(TodoStatus.COMPLETED, updatedTodo.status)
    }

    @Test
    fun `should delete a todo`() {
        // Given - Create a todo first
        val todo = Todo(
            title = "Todo to Delete",
            description = "This todo will be deleted",
            status = TodoStatus.PENDING
        )
        val savedTodo = todoRepository.save(todo)
        assertNotNull(todoRepository.findById(savedTodo.id!!).orElse(null))

        // When - Delete the todo
        mockMvc.perform(delete("/api/todos/${savedTodo.id}"))
            .andExpect(status().isNoContent)

        // Then - Verify it's gone
        mockMvc.perform(get("/api/todos/${savedTodo.id}"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should get all todos with pagination`() {
        // Given - Create multiple todos
        for (i in 1..15) {
            todoRepository.save(
                Todo(
                    title = "Todo $i",
                    description = "Description $i",
                    status = if (i % 2 == 0) TodoStatus.COMPLETED else TodoStatus.PENDING
                )
            )
        }

        // When/Then - Get first page with 10 items
        mockMvc.perform(get("/api/todos?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(10))
            .andExpect(jsonPath("$.totalElements").value(15))

        // When/Then - Get second page with remaining items
        mockMvc.perform(get("/api/todos?page=1&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(5))
    }

    @Test
    fun `should get todos by status`() {
        // Given - Create todos with different statuses
        for (i in 1..10) {
            todoRepository.save(
                Todo(
                    title = "Todo $i",
                    description = "Description $i",
                    status = if (i % 2 == 0) TodoStatus.COMPLETED else TodoStatus.PENDING
                )
            )
        }

        // When/Then - Get only completed todos
        mockMvc.perform(get("/api/todos?status=completed"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalElements").value(5))
            .andExpect(jsonPath("$.content[0].status").value("completed"))

        // When/Then - Get only pending todos
        mockMvc.perform(get("/api/todos?status=pending"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalElements").value(5))
            .andExpect(jsonPath("$.content[0].status").value("pending"))
    }
} 