package dev.olek.kordon.network

import com.nordlocker.domain.interfaces.TodoNetworkService
import com.nordlocker.domain.models.Todo
import dev.olek.kordon.network.mapper.TodoResponseMapper
import dev.olek.kordon.network.response.TodoListResponse
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal class TodoNetworkServiceImpl(
    private val client: ApiClient,
    private val todoResponseMapper: TodoResponseMapper
) : TodoNetworkService {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getAll(): List<Todo> {
        return getTodoList().data?.map(todoResponseMapper::mapToTodoModel) ?: emptyList()
    }

    private suspend fun getTodoList(): TodoListResponse = getResult(
        onCall = { client.httpClient.get { url { encodedPath = "public-api/todos" } } }
    )

    private suspend inline fun <reified T : Any> getResult(
        crossinline onCall: suspend () -> HttpResponse,
    ): T {
        val response = onCall()

        if (!response.status.isSuccess()) throw Exception("Error")
        else return json.decodeFromString(response.readText())
    }
}