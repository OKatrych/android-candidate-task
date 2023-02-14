package dev.olek.kordon.network

import dev.olek.kordon.network.mapper.TodoResponseMapper
import dev.olek.kordon.network.response.TodoResponse
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class TodoResponseMapperTest {

    private val mapper = TodoResponseMapper()
    private val testResponse = TodoResponse(
        id = Random.nextInt(),
        title = "TodoTitle",
        status = "completed",
        dueOn = "2023-03-04T00:00:00.000+05:30"
    )

    @Test(expected = IllegalArgumentException::class)
    fun testMapperCrashesOnIncorrectInput() {
        mapper.mapToTodoModel(TodoResponse())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMapperCrashesOnIncorrectStatus() {
        mapper.mapToTodoModel(testResponse.copy(status = "random"))
    }

    @Test
    fun testMapperMapsCorrectInput() {
        val result = mapper.mapToTodoModel(testResponse)

        assert(result.id == testResponse.id)
        assert(result.title == testResponse.title)
        assert(result.isCompleted)
        assert(
            result.dueDate == LocalDateTime.parse(
                testResponse.dueOn,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            )
        )
    }
}
