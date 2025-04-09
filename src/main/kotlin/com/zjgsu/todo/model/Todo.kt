package com.zjgsu.todo.model

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "todos")
data class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    
    @Column(nullable = false)
    var title: String,
    
    @Column(columnDefinition = "TEXT")
    var description: String? = null,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TodoStatus = TodoStatus.PENDING,
    
    @Column(name = "due_date")
    var dueDate: OffsetDateTime? = null,
    
    @Column(name = "category_id")
    var categoryId: UUID? = null,
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "todo_tags",
        joinColumns = [JoinColumn(name = "todo_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableSet<Tag> = mutableSetOf(),
    
    @Column(name = "memo_id")
    var memoId: UUID? = null
)

enum class TodoStatus {
    PENDING, IN_PROGRESS, COMPLETED;
    
    companion object {
        fun fromString(value: String): TodoStatus {
            return when(value.lowercase()) {
                "pending" -> PENDING
                "in_progress" -> IN_PROGRESS
                "completed" -> COMPLETED
                else -> throw IllegalArgumentException("Unknown status: $value")
            }
        }
    }
} 