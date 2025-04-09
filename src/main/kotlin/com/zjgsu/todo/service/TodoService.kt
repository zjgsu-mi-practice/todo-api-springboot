package com.zjgsu.todo.service

import com.zjgsu.todo.dto.CreateTodoRequest
import com.zjgsu.todo.dto.TodoResponse
import com.zjgsu.todo.dto.UpdateTodoRequest
import com.zjgsu.todo.model.Todo
import com.zjgsu.todo.model.TodoStatus
import com.zjgsu.todo.repository.TodoRepository
import com.zjgsu.todo.repository.TagRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TodoService(
    private val todoRepository: TodoRepository,
    private val tagRepository: TagRepository
) {
    fun getAllTodos(pageable: Pageable): Page<TodoResponse> {
        return todoRepository.findAll(pageable).map { TodoResponse.fromEntity(it) }
    }
    
    fun getTodosByStatus(status: String, pageable: Pageable): Page<TodoResponse> {
        val todoStatus = try {
            TodoStatus.fromString(status)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid status: $status")
        }
        return todoRepository.findByStatus(todoStatus, pageable).map { TodoResponse.fromEntity(it) }
    }
    
    fun getTodoById(id: UUID): TodoResponse {
        val todo = todoRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Todo not found with id: $id") }
        return TodoResponse.fromEntity(todo)
    }
    
    @Transactional
    fun createTodo(request: CreateTodoRequest): TodoResponse {
        val todo = Todo(
            title = request.title,
            description = request.description,
            dueDate = request.dueDate,
            categoryId = request.categoryId
        )
        
        // Add tags if provided
        if (!request.tagIds.isNullOrEmpty()) {
            val tags = tagRepository.findAllByIdIn(request.tagIds)
            todo.tags.addAll(tags)
        }
        
        val savedTodo = todoRepository.save(todo)
        return TodoResponse.fromEntity(savedTodo)
    }
    
    @Transactional
    fun updateTodo(id: UUID, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Todo not found with id: $id") }
        
        // Update fields if provided
        request.title?.let { todo.title = it }
        request.description?.let { todo.description = it }
        request.status?.let { todo.status = TodoStatus.fromString(it) }
        request.dueDate?.let { todo.dueDate = it }
        request.categoryId?.let { todo.categoryId = it }
        request.memoId?.let { todo.memoId = it }
        
        // Update tags if provided
        if (request.tagIds != null) {
            todo.tags.clear()
            val tags = tagRepository.findAllByIdIn(request.tagIds)
            todo.tags.addAll(tags)
        }
        
        val updatedTodo = todoRepository.save(todo)
        return TodoResponse.fromEntity(updatedTodo)
    }
    
    fun deleteTodo(id: UUID) {
        if (!todoRepository.existsById(id)) {
            throw EntityNotFoundException("Todo not found with id: $id")
        }
        todoRepository.deleteById(id)
    }
}