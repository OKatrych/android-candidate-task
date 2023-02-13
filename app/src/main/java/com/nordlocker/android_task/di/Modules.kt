package com.nordlocker.android_task.di

import com.nordlocker.android_task.ui.todo_details.TodoDetailsViewModel
import com.nordlocker.android_task.ui.todo_list.TodoListViewModel
import com.nordlocker.domain.interactors.GetTodoUseCase
import com.nordlocker.domain.interactors.LoadTodosUseCase
import com.nordlocker.domain.interactors.UpdateTodoUseCase
import com.nordlocker.domain.util.DefaultRetryPolicy
import com.nordlocker.domain.util.RetryPolicy
import com.nordlocker.storage.di.storageModule
import dev.olek.kordon.network.di.networkModule
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val QUALIFIER_IO_DISPATCHER = named("io_dispatcher")
val QUALIFIER_DEFAULT_DISPATCHER = named("default_dispatcher")
val QUALIFIER_UI_DISPATCHER = named("ui_dispatcher")

private val useCaseModule = module {
    factory {
        LoadTodosUseCase(
            todoNetworkService = get(),
            todoStorage = get(),
            retryPolicy = get(),
            dispatcher = get(QUALIFIER_IO_DISPATCHER),
        )
    }
    factory {
        GetTodoUseCase(
            todoStorage = get(),
            dispatcher = get(QUALIFIER_IO_DISPATCHER),
        )
    }
    factory {
        UpdateTodoUseCase(
            todoStorage = get(),
            dispatcher = get(QUALIFIER_IO_DISPATCHER),
        )
    }
}

val appModule = module {
    includes(useCaseModule, storageModule, networkModule)

    single(QUALIFIER_UI_DISPATCHER) { Dispatchers.Main }
    single(QUALIFIER_DEFAULT_DISPATCHER) { Dispatchers.Default }
    single(QUALIFIER_IO_DISPATCHER) { Dispatchers.IO }

    single<RetryPolicy> { DefaultRetryPolicy() }

    viewModel {
        TodoListViewModel(
            loadTodosUseCase = get()
        )
    }
    viewModel { params ->
        TodoDetailsViewModel(
            todoId = params.get(),
            getTodoUseCase = get(),
            updateTodoUseCase = get(),
        )
    }
}
