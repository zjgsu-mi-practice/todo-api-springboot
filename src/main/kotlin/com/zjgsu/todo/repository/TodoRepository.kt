package com.zjgsu.todo.repository

import com.zjgsu.todo.model.Todo
import com.zjgsu.todo.model.TodoStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TodoRepository : JpaRepository<Todo, UUID> {
    fun findByStatus(status: TodoStatus, pageable: Pageable): Page<Todo>
} 