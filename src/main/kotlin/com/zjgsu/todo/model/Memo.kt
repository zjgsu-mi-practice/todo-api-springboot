package com.zjgsu.todo.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "memos")
data class Memo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    
    @Column(columnDefinition = "TEXT")
    var content: String? = null,
    
    @ElementCollection
    @CollectionTable(
        name = "memo_attachments",
        joinColumns = [JoinColumn(name = "memo_id")]
    )
    @Column(name = "attachment_url")
    var attachments: MutableList<String> = mutableListOf()
) 