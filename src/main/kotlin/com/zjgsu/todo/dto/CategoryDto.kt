package com.zjgsu.todo.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.zjgsu.todo.model.Category
import jakarta.validation.constraints.NotBlank
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategoryResponse(
    val id: UUID?,
    val name: String,
    val color: String?
) {
    companion object {
        fun fromEntity(category: Category): CategoryResponse {
            return CategoryResponse(
                id = category.id,
                name = category.name,
                color = category.color
            )
        }
    }
}

data class CreateCategoryRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    val color: String? = null
)

data class UpdateCategoryRequest(
    val name: String? = null,
    val color: String? = null
) 