package com.zjgsu.todo.service

import com.zjgsu.todo.dto.CreateMemoRequest
import com.zjgsu.todo.dto.MemoResponse
import com.zjgsu.todo.dto.UpdateMemoRequest
import com.zjgsu.todo.model.Memo
import com.zjgsu.todo.repository.MemoRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemoService(private val memoRepository: MemoRepository) {
    
    fun getMemoById(id: UUID): MemoResponse {
        val memo = memoRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Memo not found with id: $id") }
        return MemoResponse.fromEntity(memo)
    }
    
    @Transactional
    fun createMemo(request: CreateMemoRequest): MemoResponse {
        val memo = Memo(
            content = request.content,
            attachments = request.attachments?.toMutableList() ?: mutableListOf()
        )
        
        val savedMemo = memoRepository.save(memo)
        return MemoResponse.fromEntity(savedMemo)
    }
    
    @Transactional
    fun updateMemo(id: UUID, request: UpdateMemoRequest): MemoResponse {
        val memo = memoRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Memo not found with id: $id") }
        
        request.content?.let { memo.content = it }
        
        if (request.attachments != null) {
            memo.attachments.clear()
            memo.attachments.addAll(request.attachments)
        }
        
        val updatedMemo = memoRepository.save(memo)
        return MemoResponse.fromEntity(updatedMemo)
    }
    
    fun deleteMemo(id: UUID) {
        if (!memoRepository.existsById(id)) {
            throw EntityNotFoundException("Memo not found with id: $id")
        }
        memoRepository.deleteById(id)
    }
} 