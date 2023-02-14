package com.nordlocker.domain

import com.nordlocker.domain.interactors.LoadTodosUseCase
import com.nordlocker.domain.interfaces.TodoNetworkService
import com.nordlocker.domain.interfaces.TodoStorageService
import com.nordlocker.domain.models.Todo
import com.nordlocker.domain.util.NoRetryPolicy
import com.nordlocker.domain.util.RetryPolicy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.random.Random

class LoadTodosUseCaseTest {

    private val networkService: TodoNetworkService = mockk {
        coEvery { getAll() } returns listOf()
    }
    private val storageService: TodoStorageService = mockk {
        every { observeAll() } returns flowOf()
        coEvery { updateOrCreate(any()) } returns Unit
    }
    private val retryPolicy: RetryPolicy = NoRetryPolicy()
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(scheduler)
    private val useCase: LoadTodosUseCase = LoadTodosUseCase(
        networkService,
        storageService,
        retryPolicy,
        dispatcher
    )

    private val testTodos = listOf(generateTodo(), generateTodo(), generateTodo(), generateTodo())

    @Test
    fun testNetworkServiceIsCalled() = runTest {
        useCase.loadTodos().collect()
        coVerify { networkService.getAll() }
    }

    @Test
    fun testNetworkTodosAreSaved() = runTest {
        coEvery { networkService.getAll() } returns testTodos
        useCase.loadTodos().collect()
        coVerify { storageService.updateOrCreate(testTodos) }
    }

    @Test
    fun testTodosAreReturnedFromLocalStorage() = runTest {
        coEvery { storageService.observeAll() } returns flowOf(testTodos)
        val returnedTodos = useCase.loadTodos().last()
        assert(testTodos == returnedTodos)
    }

    private fun generateTodo(): Todo {
        val date = LocalDateTime.now().minusHours(Random.nextLong(0, 48))
        return Todo(
            id = Random.nextInt(),
            title = "Todo title${Random.nextInt()}",
            isCompleted = Random.nextBoolean(),
            createdAt = date,
            updatedAt = date,
            dueDate = LocalDateTime.now().plusDays(Random.nextLong(0, 7)),
        )
    }
}