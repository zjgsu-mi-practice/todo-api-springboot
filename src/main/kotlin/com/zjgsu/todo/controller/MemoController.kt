package com.zjgsu.todo.controller

import com.zjgsu.todo.dto.CreateMemoRequest
import com.zjgsu.todo.dto.MemoResponse
import com.zjgsu.todo.dto.UpdateMemoRequest
import com.zjgsu.todo.service.MemoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/memos")
class MemoController(private val memoService: MemoService) {
    
    @GetMapping("/{id}")
    fun getMemoById(@PathVariable id: UUID): ResponseEntity<MemoResponse> {
        return ResponseEntity.ok(memoService.getMemoById(id))
    }
    
    @PostMapping
    fun createMemo(@RequestBody request: CreateMemoRequest): ResponseEntity<MemoResponse> {
        val createdMemo = memoService.createMemo(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMemo)
    }
    
    @PutMapping("/{id}")
    fun updateMemo(
        @PathVariable id: UUID,
        @RequestBody request: UpdateMemoRequest
    ): ResponseEntity<MemoResponse> {
        return ResponseEntity.ok(memoService.updateMemo(id, request))
    }
    
    @DeleteMapping("/{id}")
    fun deleteMemo(@PathVariable id: UUID): ResponseEntity<Unit> {
        memoService.deleteMemo(id)
        return ResponseEntity.noContent().build()
    }
} 