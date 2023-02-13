package com.nordlocker.domain.interactors

import com.nordlocker.domain.interfaces.TodoStorageService
import com.nordlocker.domain.models.Todo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetTodoUseCase(
    private val todoStorage: TodoStorageService,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend fun getTodo(id: Int): Todo = withContext(dispatcher) {
        todoStorage.getById(id)
    }
}
