package com.zjgsu.todo.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "categories")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    
    @Column(nullable = false, unique = true)
    var name: String,
    
    @Column
    var color: String? = null
) 