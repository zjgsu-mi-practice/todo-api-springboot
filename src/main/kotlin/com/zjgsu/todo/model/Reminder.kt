package com.zjgsu.todo.model

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "reminders")
data class Reminder(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    
    @Column(name = "todo_id")
    var todoId: UUID,
    
    @Column(nullable = false)
    var time: OffsetDateTime,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "notify_method")
    var notifyMethod: NotifyMethod = NotifyMethod.PUSH
)

enum class NotifyMethod {
    EMAIL, PUSH, SMS;
    
    companion object {
        fun fromString(value: String): NotifyMethod {
            return when(value.lowercase()) {
                "email" -> EMAIL
                "push" -> PUSH
                "sms" -> SMS
                else -> throw IllegalArgumentException("Unknown notify method: $value")
            }
        }
    }
} 