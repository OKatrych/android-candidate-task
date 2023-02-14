package com.nordlocker.android_task.ui.todo_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordlocker.domain.interactors.GetTodoUseCase
import com.nordlocker.domain.interactors.UpdateTodoUseCase
import com.nordlocker.domain.models.Todo
import com.nordlocker.domain.util.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val DEFAULT_TIMEOUT = 5000L

class TodoDetailsViewModel(
    todoId: Int,
    getTodoUseCase: GetTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
) : ViewModel() {

    val todoState: StateFlow<TodoState> = getTodoUseCase.observeTodo(todoId)
        .asResult()
        .map { todoResult ->
            if (todoResult.isSuccess) {
                TodoState.Loaded(todoResult.getOrThrow())
            } else {
                TodoState.Error(todoResult.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = TodoState.Loading,
            // Use timeout to avoid cancellation on screen rotation
            started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
        )

    private val todo: Flow<Todo> = todoState.filterIsInstance<TodoState.Loaded>().map { it.todo }

    fun setCompleted(isCompleted: Boolean) {
        viewModelScope.launch {
            updateTodoUseCase.updateTodo(
                todo.first().copy(isCompleted = isCompleted)
            )
        }
    }

    fun onTodoTitleChange(newTitle: String) {
        viewModelScope.launch {
            updateTodoUseCase.updateTodo(
                todo.first().copy(title = newTitle)
            )
        }
    }

    sealed class TodoState {
        object Loading : TodoState()
        data class Loaded(val todo: Todo) : TodoState()
        data class Error(val error: Throwable) : TodoState()
    }
}
