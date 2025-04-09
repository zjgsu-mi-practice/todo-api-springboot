package com.zjgsu.todo.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.zjgsu.todo.model.Todo
import com.zjgsu.todo.model.TodoStatus
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TodoResponse(
    val id: UUID?,
    val title: String,
    val description: String?,
    val status: String,
    val dueDate: OffsetDateTime?,
    val categoryId: UUID?,
    val tagIds: List<UUID>?,
    val memoId: UUID?
) {
    companion object {
        fun fromEntity(todo: Todo): TodoResponse {
            return TodoResponse(
                id = todo.id,
                title = todo.title,
                description = todo.description,
                status = todo.status.name.lowercase(),
                dueDate = todo.dueDate,
                categoryId = todo.categoryId,
                tagIds = if (todo.tags.isEmpty()) null else todo.tags.map { it.id!! }.toList(),
                memoId = todo.memoId
            )
        }
    }
}

data class CreateTodoRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,
    
    val description: String? = null,
    val categoryId: UUID? = null,
    val tagIds: List<UUID>? = null,
    val dueDate: OffsetDateTime? = null
)

data class UpdateTodoRequest(
    val title: String? = null,
    val description: String? = null,
    val status: String? = null,
    val dueDate: OffsetDateTime? = null,
    val categoryId: UUID? = null,
    val tagIds: List<UUID>? = null,
    val memoId: UUID? = null
) 