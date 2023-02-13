package com.nordlocker.android_task.ui.todo_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordlocker.domain.interactors.GetTodoUseCase
import com.nordlocker.domain.interactors.UpdateTodoUseCase
import com.nordlocker.domain.models.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoDetailsViewModel(
    private val todoId: Int,
    private val getTodoUseCase: GetTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
) : ViewModel() {

    private val mutableTodoState: MutableStateFlow<TodoState> = MutableStateFlow(TodoState.Loading)
    val todoState: StateFlow<TodoState> = mutableTodoState.asStateFlow()

    init {
        loadTodo(todoId)
    }

    fun setCompleted(isCompleted: Boolean) {
        // NOTE: Not perfect but should work for this case
        viewModelScope.launch {
            val todo = (mutableTodoState.value as? TodoState.Loaded)?.todo
            if (todo != null) {
                val updatedTodo = todo.copy(isCompleted = isCompleted)
                // Update UI and not wait for the DB to update
                mutableTodoState.update { currentState ->
                    if (currentState is TodoState.Loaded) {
                        currentState.copy(updatedTodo)
                    } else {
                        currentState
                    }
                }
                updateTodoUseCase.updateTodo(updatedTodo)
            }
        }
    }

    private fun loadTodo(todoId: Int) {
        viewModelScope.launch {
            val todo = getTodoUseCase.getTodo(todoId)
            mutableTodoState.update {
                TodoState.Loaded(todo)
            }
        }
    }

    sealed class TodoState {
        object Loading: TodoState()
        data class Loaded(val todo: Todo): TodoState()
        data class Error(val error: Throwable): TodoState()
    }
}