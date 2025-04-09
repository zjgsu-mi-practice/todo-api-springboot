package com.zjgsu.todo.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.zjgsu.todo.model.Memo
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MemoResponse(
    val id: UUID?,
    val content: String?,
    val attachments: List<String>?
) {
    companion object {
        fun fromEntity(memo: Memo): MemoResponse {
            return MemoResponse(
                id = memo.id,
                content = memo.content,
                attachments = if (memo.attachments.isEmpty()) null else memo.attachments
            )
        }
    }
}

data class CreateMemoRequest(
    val content: String? = null,
    val attachments: List<String>? = null
)

data class UpdateMemoRequest(
    val content: String? = null,
    val attachments: List<String>? = null
) 