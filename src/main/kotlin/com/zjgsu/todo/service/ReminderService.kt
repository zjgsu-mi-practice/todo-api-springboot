package com.zjgsu.todo.service

import com.zjgsu.todo.dto.CreateReminderRequest
import com.zjgsu.todo.dto.ReminderResponse
import com.zjgsu.todo.dto.UpdateReminderRequest
import com.zjgsu.todo.model.NotifyMethod
import com.zjgsu.todo.model.Reminder
import com.zjgsu.todo.repository.ReminderRepository
import com.zjgsu.todo.repository.TodoRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ReminderService(
    private val reminderRepository: ReminderRepository,
    private val todoRepository: TodoRepository
) {
    
    fun getReminderById(id: UUID): ReminderResponse {
        val reminder = reminderRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Reminder not found with id: $id") }
        return ReminderResponse.fromEntity(reminder)
    }
    
    fun getRemindersByTodoId(todoId: UUID): List<ReminderResponse> {
        return reminderRepository.findAllByTodoId(todoId).map { ReminderResponse.fromEntity(it) }
    }
    
    @Transactional
    fun createReminder(request: CreateReminderRequest): ReminderResponse {
        // Verify todo exists
        if (!todoRepository.existsById(request.todoId)) {
            throw EntityNotFoundException("Todo not found with id: ${request.todoId}")
        }
        
        val notifyMethod = request.notifyMethod?.let {
            try {
                NotifyMethod.fromString(it)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid notification method: $it")
            }
        } ?: NotifyMethod.PUSH
        
        val reminder = Reminder(
            todoId = request.todoId,
            time = request.time,
            notifyMethod = notifyMethod
        )
        
        val savedReminder = reminderRepository.save(reminder)
        return ReminderResponse.fromEntity(savedReminder)
    }
    
    @Transactional
    fun updateReminder(id: UUID, request: UpdateReminderRequest): ReminderResponse {
        val reminder = reminderRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Reminder not found with id: $id") }
        
        request.time?.let { reminder.time = it }
        
        request.notifyMethod?.let {
            try {
                reminder.notifyMethod = NotifyMethod.fromString(it)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid notification method: $it")
            }
        }
        
        val updatedReminder = reminderRepository.save(reminder)
        return ReminderResponse.fromEntity(updatedReminder)
    }
    
    fun deleteReminder(id: UUID) {
        if (!reminderRepository.existsById(id)) {
            throw EntityNotFoundException("Reminder not found with id: $id")
        }
        reminderRepository.deleteById(id)
    }
} 