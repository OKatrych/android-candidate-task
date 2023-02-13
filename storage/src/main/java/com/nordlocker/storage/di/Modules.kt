package com.nordlocker.storage.di

import com.nordlocker.domain.interfaces.TodoStorageService
import com.nordlocker.storage.DatabaseCreator
import com.nordlocker.storage.mapper.TodoEntityMapper
import com.nordlocker.storage.todo.TodoStorageImpl
import org.koin.dsl.module

val storageModule = module {
    single { DatabaseCreator.create(get()) }
    factory { TodoEntityMapper() }
    single<TodoStorageService> {
        TodoStorageImpl(
            database = get(),
            todoEntityMapper = get(),
        )
    }
}