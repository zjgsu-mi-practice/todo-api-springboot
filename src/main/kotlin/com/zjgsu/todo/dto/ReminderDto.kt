package com.zjgsu.todo.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.zjgsu.todo.model.NotifyMethod
import com.zjgsu.todo.model.Reminder
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReminderResponse(
    val id: UUID?,
    val todoId: UUID,
    val time: OffsetDateTime,
    val notifyMethod: String
) {
    companion object {
        fun fromEntity(reminder: Reminder): ReminderResponse {
            return ReminderResponse(
                id = reminder.id,
                todoId = reminder.todoId,
                time = reminder.time,
                notifyMethod = reminder.notifyMethod.name.lowercase()
            )
        }
    }
}

data class CreateReminderRequest(
    @field:NotNull(message = "Todo ID is required")
    val todoId: UUID,
    
    @field:NotNull(message = "Time is required")
    val time: OffsetDateTime,
    
    val notifyMethod: String? = "push"
)

data class UpdateReminderRequest(
    val time: OffsetDateTime? = null,
    val notifyMethod: String? = null
)