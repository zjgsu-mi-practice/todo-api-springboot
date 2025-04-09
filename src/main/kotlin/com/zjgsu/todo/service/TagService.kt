package com.zjgsu.todo.service

import com.zjgsu.todo.dto.CreateTagRequest
import com.zjgsu.todo.dto.TagResponse
import com.zjgsu.todo.dto.UpdateTagRequest
import com.zjgsu.todo.model.Tag
import com.zjgsu.todo.repository.TagRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TagService(private val tagRepository: TagRepository) {
    
    fun getAllTags(): List<TagResponse> {
        return tagRepository.findAll().map { TagResponse.fromEntity(it) }
    }
    
    fun getTagById(id: UUID): TagResponse {
        val tag = tagRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Tag not found with id: $id") }
        return TagResponse.fromEntity(tag)
    }
    
    @Transactional
    fun createTag(request: CreateTagRequest): TagResponse {
        // Check if tag with the same name already exists
        if (tagRepository.findByName(request.name) != null) {
            throw DataIntegrityViolationException("Tag with name '${request.name}' already exists")
        }
        
        val tag = Tag(name = request.name)
        val savedTag = tagRepository.save(tag)
        return TagResponse.fromEntity(savedTag)
    }
    
    @Transactional
    fun updateTag(id: UUID, request: UpdateTagRequest): TagResponse {
        val tag = tagRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Tag not found with id: $id") }
        
        // Check name uniqueness if updating name
        if (request.name != null && request.name != tag.name) {
            if (tagRepository.findByName(request.name) != null) {
                throw DataIntegrityViolationException("Tag with name '${request.name}' already exists")
            }
            tag.name = request.name
        }
        
        val updatedTag = tagRepository.save(tag)
        return TagResponse.fromEntity(updatedTag)
    }
    
    fun deleteTag(id: UUID) {
        if (!tagRepository.existsById(id)) {
            throw EntityNotFoundException("Tag not found with id: $id")
        }
        tagRepository.deleteById(id)
    }
}