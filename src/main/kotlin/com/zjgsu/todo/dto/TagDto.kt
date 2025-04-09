package com.zjgsu.todo.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.zjgsu.todo.model.Tag
import jakarta.validation.constraints.NotBlank
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TagResponse(
    val id: UUID?,
    val name: String
) {
    companion object {
        fun fromEntity(tag: Tag): TagResponse {
            return TagResponse(
                id = tag.id,
                name = tag.name
            )
        }
    }
}

data class CreateTagRequest(
    @field:NotBlank(message = "Name is required")
    val name: String
)

data class UpdateTagRequest(
    val name: String? = null
) 