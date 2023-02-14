package com.nordlocker.domain.interfaces

import com.nordlocker.domain.models.Todo
import kotlinx.coroutines.flow.Flow

interface TodoStorageService {
    suspend fun updateOrCreate(list: List<Todo>)
    suspend fun getAll(): List<Todo>
    fun observeAll(): Flow<List<Todo>>
    fun observeById(id: Int): Flow<Todo>
}