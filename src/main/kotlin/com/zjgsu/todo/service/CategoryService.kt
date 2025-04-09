package com.zjgsu.todo.service

import com.zjgsu.todo.dto.CategoryResponse
import com.zjgsu.todo.dto.CreateCategoryRequest
import com.zjgsu.todo.dto.UpdateCategoryRequest
import com.zjgsu.todo.model.Category
import com.zjgsu.todo.repository.CategoryRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {
    
    fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAll().map { CategoryResponse.fromEntity(it) }
    }
    
    fun getCategoryById(id: UUID): CategoryResponse {
        val category = categoryRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Category not found with id: $id") }
        return CategoryResponse.fromEntity(category)
    }
    
    @Transactional
    fun createCategory(request: CreateCategoryRequest): CategoryResponse {
        // Check if category with the same name already exists
        if (categoryRepository.findByName(request.name) != null) {
            throw DataIntegrityViolationException("Category with name '${request.name}' already exists")
        }
        
        val category = Category(
            name = request.name,
            color = request.color
        )
        
        val savedCategory = categoryRepository.save(category)
        return CategoryResponse.fromEntity(savedCategory)
    }
    
    @Transactional
    fun updateCategory(id: UUID, request: UpdateCategoryRequest): CategoryResponse {
        val category = categoryRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Category not found with id: $id") }
        
        // Check name uniqueness if updating name
        if (request.name != null && request.name != category.name) {
            if (categoryRepository.findByName(request.name) != null) {
                throw DataIntegrityViolationException("Category with name '${request.name}' already exists")
            }
            category.name = request.name
        }
        
        request.color?.let { category.color = it }
        
        val updatedCategory = categoryRepository.save(category)
        return CategoryResponse.fromEntity(updatedCategory)
    }
    
    fun deleteCategory(id: UUID) {
        if (!categoryRepository.existsById(id)) {
            throw EntityNotFoundException("Category not found with id: $id")
        }
        categoryRepository.deleteById(id)
    }
}