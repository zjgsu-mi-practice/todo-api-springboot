package com.zjgsu.todo.repository

import com.zjgsu.todo.model.Memo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemoRepository : JpaRepository<Memo, UUID> 