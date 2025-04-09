package com.zjgsu.todo.controller

import com.zjgsu.todo.dto.CreateTagRequest
import com.zjgsu.todo.dto.TagResponse
import com.zjgsu.todo.dto.UpdateTagRequest
import com.zjgsu.todo.service.TagService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/tags")
class TagController(private val tagService: TagService) {
    
    @GetMapping
    fun getAllTags(): ResponseEntity<List<TagResponse>> {
        return ResponseEntity.ok(tagService.getAllTags())
    }
    
    @GetMapping("/{id}")
    fun getTagById(@PathVariable id: UUID): ResponseEntity<TagResponse> {
        return ResponseEntity.ok(tagService.getTagById(id))
    }
    
    @PostMapping
    fun createTag(@Valid @RequestBody request: CreateTagRequest): ResponseEntity<TagResponse> {
        val createdTag = tagService.createTag(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag)
    }
    
    @PutMapping("/{id}")
    fun updateTag(
        @PathVariable id: UUID,
        @RequestBody request: UpdateTagRequest
    ): ResponseEntity<TagResponse> {
        return ResponseEntity.ok(tagService.updateTag(id, request))
    }
    
    @DeleteMapping("/{id}")
    fun deleteTag(@PathVariable id: UUID): ResponseEntity<Unit> {
        tagService.deleteTag(id)
        return ResponseEntity.noContent().build()
    }
}