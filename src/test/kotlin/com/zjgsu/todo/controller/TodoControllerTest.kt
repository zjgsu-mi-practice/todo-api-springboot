package com.zjgsu.todo.controller

import com.zjgsu.todo.dto.CreateTodoRequest
import com.zjgsu.todo.dto.TodoResponse
import com.zjgsu.todo.dto.UpdateTodoRequest
import com.zjgsu.todo.service.TodoService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import java.time.OffsetDateTime
import java.util.*

class TodoControllerTest {

    private val todoService: TodoService = mockk()
    private val todoController = TodoController(todoService)
    
    private val todoId = UUID.randomUUID()
    private val now = OffsetDateTime.now()
    
    @Test
    fun `should return all todos`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val todoResponse = TodoResponse(
            id = todoId,
            title = "Test Todo",
            description = "Test Description",
            status = "pending",
            dueDate = now,
            categoryId = null,
            tagIds = null,
            memoId = null
        )
        val page = PageImpl(listOf(todoResponse))
        
        every { todoService.getAllTodos(pageable) } returns page
        
        // When
        val response = todoController.getAllTodos(null, pageable)
        
        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(page, response.body)
        verify(exactly = 1) { todoService.getAllTodos(pageable) }
    }
    
    @Test
    fun `should return todos by status`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val status = "completed"
        val todoResponse = TodoResponse(
            id = todoId,
            title = "Test Todo",
            description = "Test Description",
            status = status,
            dueDate = now,
            categoryId = null,
            tagIds = null,
            memoId = null
        )
        val page = PageImpl(listOf(todoResponse))
        
        every { todoService.getTodosByStatus(status, pageable) } returns page
        
        // When
        val response = todoController.getAllTodos(status, pageable)
        
        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(page, response.body)
        verify(exactly = 1) { todoService.getTodosByStatus(status, pageable) }
    }
    
    @Test
    fun `should return todo by id`() {
        // Given
        val todoResponse = TodoResponse(
            id = todoId,
            title = "Test Todo",
            description = "Test Description",
            status = "pending",
            dueDate = now,
            categoryId = null,
            tagIds = null,
            memoId = null
        )
        
        every { todoService.getTodoById(todoId) } returns todoResponse
        
        // When
        val response = todoController.getTodoById(todoId)
        
        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(todoResponse, response.body)
        verify(exactly = 1) { todoService.getTodoById(todoId) }
    }
    
    @Test
    fun `should create todo`() {
        // Given
        val createRequest = CreateTodoRequest(
            title = "New Todo",
            description = "New Description",
            dueDate = now
        )
        
        val todoResponse = TodoResponse(
            id = todoId,
            title = createRequest.title,
            description = createRequest.description,
            status = "pending",
            dueDate = createRequest.dueDate,
            categoryId = null,
            tagIds = null,
            memoId = null
        )
        
        every { todoService.createTodo(createRequest) } returns todoResponse
        
        // When
        val response = todoController.createTodo(createRequest)
        
        // Then
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(todoResponse, response.body)
        verify(exactly = 1) { todoService.createTodo(createRequest) }
    }
    
    @Test
    fun `should update todo`() {
        // Given
        val updateRequest = UpdateTodoRequest(
            title = "Updated Title",
            status = "completed"
        )
        
        val todoResponse = TodoResponse(
            id = todoId,
            title = updateRequest.title!!,
            description = "Test Description",
            status = updateRequest.status!!,
            dueDate = now,
            categoryId = null,
            tagIds = null,
            memoId = null
        )
        
        every { todoService.updateTodo(todoId, updateRequest) } returns todoResponse
        
        // When
        val response = todoController.updateTodo(todoId, updateRequest)
        
        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(todoResponse, response.body)
        verify(exactly = 1) { todoService.updateTodo(todoId, updateRequest) }
    }
    
    @Test
    fun `should delete todo`() {
        // Given
        every { todoService.deleteTodo(todoId) } returns Unit
        
        // When
        val response = todoController.deleteTodo(todoId)
        
        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { todoService.deleteTodo(todoId) }
    }
} 