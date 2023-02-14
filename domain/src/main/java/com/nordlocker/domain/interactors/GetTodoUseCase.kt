package com.nordlocker.domain.interactors

import com.nordlocker.domain.interfaces.TodoStorageService
import com.nordlocker.domain.models.Todo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetTodoUseCase(
    private val todoStorage: TodoStorageService,
    private val dispatcher: CoroutineDispatcher,
) {

    fun observeTodo(id: Int): Flow<Todo> = todoStorage.observeById(id).flowOn(dispatcher)
}
