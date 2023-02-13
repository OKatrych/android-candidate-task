package com.nordlocker.domain.interactors

import com.nordlocker.domain.interfaces.TodoNetworkService
import com.nordlocker.domain.interfaces.TodoStorageService
import com.nordlocker.domain.models.Todo
import com.nordlocker.domain.util.RetryPolicy
import com.nordlocker.domain.util.retryWithPolicy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import java.io.IOException

/**
 * Loads todos from the local storage and then tries to load latest values from the network
 * and saving them to the local storage
 */
class LoadTodosUseCase(
    private val todoNetworkService: TodoNetworkService,
    private val todoStorage: TodoStorageService,
    private val retryPolicy: RetryPolicy,
    private val dispatcher: CoroutineDispatcher,
) {

    fun loadTodos(): Flow<List<Todo>> = flow {
        // Load available todos from the local storage
        emit(todoStorage.getAll())

        // Try to load the newest todos from the network and save them to local storage
        getTodosFromNetwork().onEach {
            todoStorage.updateOrCreate(it)
        }.collect()

        // Subscribe for all storage changes
        emitAll(todoStorage.observeAll())
    }.flowOn(dispatcher)

    private fun getTodosFromNetwork(): Flow<List<Todo>> = flow {
        emit(todoNetworkService.getAll())
    }.retryWithPolicy(retryPolicy) { cause ->
        // Retry only when connection issue occurred
        cause is IOException
    }
}