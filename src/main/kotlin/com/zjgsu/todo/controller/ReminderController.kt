package com.zjgsu.todo.controller

import com.zjgsu.todo.dto.CreateReminderRequest
import com.zjgsu.todo.dto.ReminderResponse
import com.zjgsu.todo.dto.UpdateReminderRequest
import com.zjgsu.todo.service.ReminderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/reminders")
class ReminderController(private val reminderService: ReminderService) {
    
    @GetMapping("/{id}")
    fun getReminderById(@PathVariable id: UUID): ResponseEntity<ReminderResponse> {
        return ResponseEntity.ok(reminderService.getReminderById(id))
    }
    
    @GetMapping("/todo/{todoId}")
    fun getRemindersByTodoId(@PathVariable todoId: UUID): ResponseEntity<List<ReminderResponse>> {
        return ResponseEntity.ok(reminderService.getRemindersByTodoId(todoId))
    }
    
    @PostMapping
    fun createReminder(@Valid @RequestBody request: CreateReminderRequest): ResponseEntity<ReminderResponse> {
        val createdReminder = reminderService.createReminder(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReminder)
    }
    
    @PutMapping("/{id}")
    fun updateReminder(
        @PathVariable id: UUID,
        @RequestBody request: UpdateReminderRequest
    ): ResponseEntity<ReminderResponse> {
        return ResponseEntity.ok(reminderService.updateReminder(id, request))
    }
    
    @DeleteMapping("/{id}")
    fun deleteReminder(@PathVariable id: UUID): ResponseEntity<Unit> {
        reminderService.deleteReminder(id)
        return ResponseEntity.noContent().build()
    }
} 