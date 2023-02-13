package com.nordlocker.domain.models

import java.time.LocalDateTime

data class Todo(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val dueDate: LocalDateTime,
)