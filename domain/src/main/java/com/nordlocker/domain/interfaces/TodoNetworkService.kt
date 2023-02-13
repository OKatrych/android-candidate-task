package com.nordlocker.domain.interfaces

import com.nordlocker.domain.models.Todo

interface TodoNetworkService {
    suspend fun getAll(): List<Todo>
}