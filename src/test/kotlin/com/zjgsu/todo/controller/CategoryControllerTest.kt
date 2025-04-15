package com.zjgsu.todo.controller

import com.zjgsu.todo.dto.CategoryResponse
import com.zjgsu.todo.dto.CreateCategoryRequest
import com.zjgsu.todo.service.CategoryService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.*

class CategoryControllerTest {

    private val categoryService: CategoryService = mockk()
    private val categoryController = CategoryController(categoryService)
    
    private val categoryId = UUID.randomUUID()
    
    @Test
    fun `should return all categories`() {
        // Given
        val categoryResponse = CategoryResponse(
            id = categoryId,
            name = "Test Category",
            color = "#FF5733"
        )
        val categories = listOf(categoryResponse)
        
        every { categoryService.getAllCategories() } returns categories
        
        // When
        val response = categoryController.getAllCategories()
        
        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(categories, response.body)
        verify(exactly = 1) { categoryService.getAllCategories() }
    }
    
    @Test
    fun `should return category by id`() {
        // Given
        val categoryResponse = CategoryResponse(
            id = categoryId,
            name = "Test Category",
            color = "#FF5733"
        )
        
        every { categoryService.getCategoryById(categoryId) } returns categoryResponse
        
        // When
        val response = categoryController.getCategoryById(categoryId)
        
        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(categoryResponse, response.body)
        verify(exactly = 1) { categoryService.getCategoryById(categoryId) }
    }
    
    @Test
    fun `should create category`() {
        // Given
        val createRequest = CreateCategoryRequest(
            name = "New Category",
            color = "#FF5733"
        )
        
        val categoryResponse = CategoryResponse(
            id = categoryId,
            name = createRequest.name,
            color = createRequest.color
        )
        
        every { categoryService.createCategory(createRequest) } returns categoryResponse
        
        // When
        val response = categoryController.createCategory(createRequest)
        
        // Then
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(categoryResponse, response.body)
        verify(exactly = 1) { categoryService.createCategory(createRequest) }
    }
} 