package com.zjgsu.todo.service

import com.zjgsu.todo.dto.CreateReminderRequest
import com.zjgsu.todo.dto.ReminderResponse
import com.zjgsu.todo.dto.UpdateReminderRequest
import com.zjgsu.todo.model.NotifyMethod
import com.zjgsu.todo.model.Reminder
import com.zjgsu.todo.repository.ReminderRepository
import com.zjgsu.todo.repository.TodoRepository
import io.mockk.*
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.util.*

class ReminderServiceTest {

    private val reminderRepository: ReminderRepository = mockk()
    private val todoRepository: TodoRepository = mockk()
    private lateinit var reminderService: ReminderService
    
    private val reminderId = UUID.randomUUID()
    private val todoId = UUID.randomUUID()
    private val now = OffsetDateTime.now()
    
    @BeforeEach
    fun setup() {
        reminderService = ReminderService(reminderRepository, todoRepository)
    }
    
    @Test
    fun `should get reminder by id`() {
        // Given
        val reminder = Reminder(
            id = reminderId,
            todoId = todoId,
            time = now,
            notifyMethod = NotifyMethod.PUSH
        )
        
        every { reminderRepository.findById(reminderId) } returns Optional.of(reminder)
        
        // When
        val result = reminderService.getReminderById(reminderId)
        
        // Then
        assertEquals(reminderId, result.id)
        assertEquals(todoId, result.todoId)
        assertEquals(now, result.time)
        assertEquals("push", result.notifyMethod)
        
        verify(exactly = 1) { reminderRepository.findById(reminderId) }
    }
    
    @Test
    fun `should throw exception when reminder not found`() {
        // Given
        every { reminderRepository.findById(reminderId) } returns Optional.empty()
        
        // When/Then
        assertThrows<EntityNotFoundException> {
            reminderService.getReminderById(reminderId)
        }
        
        verify(exactly = 1) { reminderRepository.findById(reminderId) }
    }
    
    @Test
    fun `should create reminder`() {
        // Given
        val createRequest = CreateReminderRequest(
            todoId = todoId,
            time = now,
            notifyMethod = "push"
        )
        
        val reminderToSave = Reminder(
            todoId = createRequest.todoId,
            time = createRequest.time,
            notifyMethod = NotifyMethod.PUSH
        )
        
        val savedReminder = Reminder(
            id = reminderId,
            todoId = createRequest.todoId,
            time = createRequest.time,
            notifyMethod = NotifyMethod.PUSH
        )
        
        every { todoRepository.existsById(todoId) } returns true
        every { reminderRepository.save(any()) } returns savedReminder
        
        // When
        val result = reminderService.createReminder(createRequest)
        
        // Then
        assertEquals(reminderId, result.id)
        assertEquals(todoId, result.todoId)
        assertEquals(now, result.time)
        assertEquals("push", result.notifyMethod)
        
        verify(exactly = 1) { todoRepository.existsById(todoId) }
        verify(exactly = 1) { reminderRepository.save(any()) }
    }
    
    @Test
    fun `should throw exception when creating reminder for non-existent todo`() {
        // Given
        val createRequest = CreateReminderRequest(
            todoId = todoId,
            time = now,
            notifyMethod = "push"
        )
        
        every { todoRepository.existsById(todoId) } returns false
        
        // When/Then
        assertThrows<EntityNotFoundException> {
            reminderService.createReminder(createRequest)
        }
        
        verify(exactly = 1) { todoRepository.existsById(todoId) }
        verify(exactly = 0) { reminderRepository.save(any()) }
    }
    
    @Test
    fun `should update reminder`() {
        // Given
        val updateRequest = UpdateReminderRequest(
            time = now.plusDays(1),
            notifyMethod = "email"
        )
        
        val existingReminder = Reminder(
            id = reminderId,
            todoId = todoId,
            time = now,
            notifyMethod = NotifyMethod.PUSH
        )
        
        val updatedReminder = Reminder(
            id = reminderId,
            todoId = todoId,
            time = now.plusDays(1),
            notifyMethod = NotifyMethod.EMAIL
        )
        
        every { reminderRepository.findById(reminderId) } returns Optional.of(existingReminder)
        every { reminderRepository.save(any()) } returns updatedReminder
        
        // When
        val result = reminderService.updateReminder(reminderId, updateRequest)
        
        // Then
        assertEquals(reminderId, result.id)
        assertEquals(todoId, result.todoId)
        assertEquals(now.plusDays(1), result.time)
        assertEquals("email", result.notifyMethod)
        
        verify(exactly = 1) { reminderRepository.findById(reminderId) }
        verify(exactly = 1) { reminderRepository.save(any()) }
    }
    
    @Test
    fun `should delete reminder`() {
        // Given
        every { reminderRepository.existsById(reminderId) } returns true
        justRun { reminderRepository.deleteById(reminderId) }
        
        // When
        reminderService.deleteReminder(reminderId)
        
        // Then
        verify(exactly = 1) { reminderRepository.existsById(reminderId) }
        verify(exactly = 1) { reminderRepository.deleteById(reminderId) }
    }
} 