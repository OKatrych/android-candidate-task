package dev.olek.kordon.network.di

import com.nordlocker.domain.interfaces.TodoNetworkService
import dev.olek.kordon.network.ApiClient
import dev.olek.kordon.network.TodoNetworkServiceImpl
import dev.olek.kordon.network.mapper.TodoResponseMapper
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient() }
    factory { TodoResponseMapper() }
    single<TodoNetworkService> {
        TodoNetworkServiceImpl(
            client = get(),
            todoResponseMapper = get(),
        )
    }
}