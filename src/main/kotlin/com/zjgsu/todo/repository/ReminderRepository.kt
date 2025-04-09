package com.zjgsu.todo.repository

import com.zjgsu.todo.model.Reminder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReminderRepository : JpaRepository<Reminder, UUID> {
    fun findAllByTodoId(todoId: UUID): List<Reminder>
} 