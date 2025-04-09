package com.zjgsu.todo.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "tags")
data class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    
    @Column(nullable = false, unique = true)
    var name: String
) 