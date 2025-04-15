package com.zjgsu.todo.service

import com.zjgsu.todo.dto.CreateTodoRequest
import com.zjgsu.todo.dto.UpdateTodoRequest
import com.zjgsu.todo.model.Tag
import com.zjgsu.todo.model.Todo
import com.zjgsu.todo.model.TodoStatus
import com.zjgsu.todo.repository.TagRepository
import com.zjgsu.todo.repository.TodoRepository
import io.mockk.*
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.OffsetDateTime
import java.util.*

class TodoServiceTest {

    private val todoRepository: TodoRepository = mockk()
    private val tagRepository: TagRepository = mockk()
    private lateinit var todoService: TodoService
    
    private val todoId = UUID.randomUUID()
    private val tagId = UUID.randomUUID()
    private val categoryId = UUID.randomUUID()
    private val memoId = UUID.randomUUID()
    private val now = OffsetDateTime.now()
    
    @BeforeEach
    fun setup() {
        todoService = TodoService(todoRepository, tagRepository)
    }
    
    @Test
    fun `should get all todos`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val todo = Todo(
            id = todoId,
            title = "Test Todo",
            description = "Test Description",
            status = TodoStatus.PENDING,
            dueDate = now,
            categoryId = null
        )
        val todos = PageImpl(listOf(todo))
        
        every { todoRepository.findAll(pageable) } returns todos
        
        // When
        val result = todoService.getAllTodos(pageable)
        
        // Then
        assertEquals(1, result.content.size)
        assertEquals(todoId, result.content[0].id)
        assertEquals("Test Todo", result.content[0].title)
        assertEquals("Test Description", result.content[0].description)
        assertEquals("pending", result.content[0].status)
        assertEquals(now, result.content[0].dueDate)
        
        verify(exactly = 1) { todoRepository.findAll(pageable) }
    }
    
    @Test
    fun `should get todos by status`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val status = "completed"
        val todoStatus = TodoStatus.COMPLETED
        val todo = Todo(
            id = todoId,
            title = "Test Todo",
            description = "Test Description",
            status = todoStatus,
            dueDate = now,
            categoryId = null
        )
        val todos = PageImpl(listOf(todo))
        
        every { todoRepository.findByStatus(todoStatus, pageable) } returns todos
        
        // When
        val result = todoService.getTodosByStatus(status, pageable)
        
        // Then
        assertEquals(1, result.content.size)
        assertEquals(todoId, result.content[0].id)
        assertEquals("completed", result.content[0].status)
        
        verify(exactly = 1) { todoRepository.findByStatus(todoStatus, pageable) }
    }
    
    @Test
    fun `should throw exception for invalid status`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val status = "invalid_status"
        
        // When/Then
        assertThrows<IllegalArgumentException> {
            todoService.getTodosByStatus(status, pageable)
        }
    }
    
    @Test
    fun `should get todo by id`() {
        // Given
        val todo = Todo(
            id = todoId,
            title = "Test Todo",
            description = "Test Description",
            status = TodoStatus.PENDING,
            dueDate = now,
            categoryId = null
        )
        
        every { todoRepository.findById(todoId) } returns Optional.of(todo)
        
        // When
        val result = todoService.getTodoById(todoId)
        
        // Then
        assertEquals(todoId, result.id)
        assertEquals("Test Todo", result.title)
        
        verify(exactly = 1) { todoRepository.findById(todoId) }
    }
    
    @Test
    fun `should throw exception when todo not found`() {
        // Given
        every { todoRepository.findById(todoId) } returns Optional.empty()
        
        // When/Then
        assertThrows<EntityNotFoundException> {
            todoService.getTodoById(todoId)
        }
        
        verify(exactly = 1) { todoRepository.findById(todoId) }
    }
    
    @Test
    fun `should create todo`() {
        // Given
        val createRequest = CreateTodoRequest(
            title = "New Todo",
            description = "New Description",
            dueDate = now,
            categoryId = categoryId,
            tagIds = listOf(tagId)
        )
        
        val tag = Tag(
            id = tagId,
            name = "Test Tag"
        )
        
        val todoToSave = Todo(
            title = createRequest.title,
            description = createRequest.description,
            dueDate = createRequest.dueDate,
            categoryId = createRequest.categoryId
        )
        todoToSave.tags.add(tag)
        
        val savedTodo = Todo(
            id = todoId,
            title = createRequest.title,
            description = createRequest.description,
            dueDate = createRequest.dueDate,
            categoryId = createRequest.categoryId
        )
        savedTodo.tags.add(tag)
        
        every { tagRepository.findAllByIdIn(listOf(tagId)) } returns listOf(tag)
        every { todoRepository.save(any()) } returns savedTodo
        
        // When
        val result = todoService.createTodo(createRequest)
        
        // Then
        assertEquals(todoId, result.id)
        assertEquals("New Todo", result.title)
        assertEquals(listOf(tagId), result.tagIds)
        
        verify(exactly = 1) { tagRepository.findAllByIdIn(listOf(tagId)) }
        verify(exactly = 1) { todoRepository.save(any()) }
    }
    
    @Test
    fun `should update todo`() {
        // Given
        val updateRequest = UpdateTodoRequest(
            title = "Updated Title",
            status = "completed",
            memoId = memoId
        )
        
        val existingTodo = Todo(
            id = todoId,
            title = "Test Todo",
            description = "Test Description",
            status = TodoStatus.PENDING,
            dueDate = now,
            categoryId = categoryId
        )
        
        val updatedTodo = Todo(
            id = todoId,
            title = "Updated Title",
            description = "Test Description",
            status = TodoStatus.COMPLETED,
            dueDate = now,
            categoryId = categoryId,
            memoId = memoId
        )
        
        every { todoRepository.findById(todoId) } returns Optional.of(existingTodo)
        every { todoRepository.save(any()) } returns updatedTodo
        
        // When
        val result = todoService.updateTodo(todoId, updateRequest)
        
        // Then
        assertEquals(todoId, result.id)
        assertEquals("Updated Title", result.title)
        assertEquals("completed", result.status)
        assertEquals(memoId, result.memoId)
        
        verify(exactly = 1) { todoRepository.findById(todoId) }
        verify(exactly = 1) { todoRepository.save(any()) }
    }
    
    @Test
    fun `should delete todo`() {
        // Given
        every { todoRepository.existsById(todoId) } returns true
        justRun { todoRepository.deleteById(todoId) }
        
        // When
        todoService.deleteTodo(todoId)
        
        // Then
        verify(exactly = 1) { todoRepository.existsById(todoId) }
        verify(exactly = 1) { todoRepository.deleteById(todoId) }
    }
    
    @Test
    fun `should throw exception when trying to delete non-existent todo`() {
        // Given
        every { todoRepository.existsById(todoId) } returns false
        
        // When/Then
        assertThrows<EntityNotFoundException> {
            todoService.deleteTodo(todoId)
        }
        
        verify(exactly = 1) { todoRepository.existsById(todoId) }
        verify(exactly = 0) { todoRepository.deleteById(any()) }
    }
} 