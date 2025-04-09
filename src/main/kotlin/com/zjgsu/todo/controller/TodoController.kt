package com.zjgsu.todo.controller

import com.zjgsu.todo.dto.CreateTodoRequest
import com.zjgsu.todo.dto.TodoResponse
import com.zjgsu.todo.dto.UpdateTodoRequest
import com.zjgsu.todo.service.TodoService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/todos")
class TodoController(private val todoService: TodoService) {
    
    @GetMapping
    fun getAllTodos(
        @RequestParam(required = false) status: String?,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<TodoResponse>> {
        val todos = if (status != null) {
            todoService.getTodosByStatus(status, pageable)
        } else {
            todoService.getAllTodos(pageable)
        }
        return ResponseEntity.ok(todos)
    }
    
    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: UUID): ResponseEntity<TodoResponse> {
        return ResponseEntity.ok(todoService.getTodoById(id))
    }
    
    @PostMapping
    fun createTodo(@Valid @RequestBody request: CreateTodoRequest): ResponseEntity<TodoResponse> {
        val createdTodo = todoService.createTodo(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo)
    }
    
    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: UUID,
        @RequestBody request: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.ok(todoService.updateTodo(id, request))
    }
    
    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: UUID): ResponseEntity<Unit> {
        todoService.deleteTodo(id)
        return ResponseEntity.noContent().build()
    }
} 