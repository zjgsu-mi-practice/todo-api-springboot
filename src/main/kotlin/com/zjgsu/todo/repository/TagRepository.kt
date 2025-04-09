package com.zjgsu.todo.repository

import com.zjgsu.todo.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagRepository : JpaRepository<Tag, UUID> {
    fun findByName(name: String): Tag?
    fun findAllByIdIn(ids: List<UUID>): List<Tag>
} 