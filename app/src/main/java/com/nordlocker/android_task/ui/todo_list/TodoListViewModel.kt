package com.nordlocker.android_task.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordlocker.domain.interactors.LoadTodosUseCase
import com.nordlocker.domain.models.Todo
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import timber.log.Timber

class TodoListViewModel(
    private val loadTodosUseCase: LoadTodosUseCase,
) : ViewModel() {

    private val todosJob = SupervisorJob()

    private val mutableTodoState: MutableStateFlow<List<Todo>> = MutableStateFlow(emptyList())
    val todoState: StateFlow<List<Todo>> = mutableTodoState.asStateFlow()

    private var sortingType: SortingType = SortingType.RecentlyCreated

    init {
        loadTodos()
    }

    fun loadTodos() {
        todosJob.cancelChildren()
        loadTodosUseCase.loadTodos().onEach { todos ->
            mutableTodoState.updateTodoState(sortingType) { todos }
        }.catch { error ->
            Timber.e(error, "Failed to load todos")
        }.launchIn(viewModelScope + todosJob)

    }

    fun changeSortingType() {
        sortingType = if (sortingType is SortingType.RecentlyCreated) {
            SortingType.UnCompletedOnTop
        } else {
            SortingType.RecentlyCreated
        }
        mutableTodoState.updateTodoState(sortingType) { it }
    }

    private inline fun MutableStateFlow<List<Todo>>.updateTodoState(
        sortingType: SortingType,
        function: (List<Todo>) -> List<Todo>,
    ) {
        update {
            function(it).sortedWith(sortingType.comparator)
        }
    }

    sealed class SortingType(val comparator: Comparator<Todo>) {
        object RecentlyCreated : SortingType(
            Comparator { o1, o2 -> o1.updatedAt.compareTo(o2.updatedAt) }
        )

        object UnCompletedOnTop : SortingType(
            Comparator { o1, o2 -> o1.isCompleted.compareTo(o2.isCompleted) }
        )
    }
}