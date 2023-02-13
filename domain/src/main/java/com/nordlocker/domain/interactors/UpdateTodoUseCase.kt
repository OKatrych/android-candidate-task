package com.nordlocker.domain.interactors

import com.nordlocker.domain.interfaces.TodoStorageService
import com.nordlocker.domain.models.Todo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateTodoUseCase(
    private val todoStorage: TodoStorageService,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend fun updateTodo(todo: Todo) = withContext(dispatcher) {
        todoStorage.updateOrCreate(listOf(todo))
    }
}
