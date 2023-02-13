package dev.olek.kordon.network.mapper

import com.nordlocker.domain.models.Todo
import dev.olek.kordon.network.response.TodoResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: create test
internal class TodoResponseMapper {

    companion object {
        private const val STATUS_COMPLETED = "completed"
        private const val STATUS_PENDING = "pending"
    }

    fun mapToTodoModel(response: TodoResponse): Todo {
        requireNotNull(response.id)
        requireNotNull(response.title)
        requireNotNull(response.status)
        requireNotNull(response.dueOn)

        val creationDate = getRandomDateFromPast()
        val dueDate = LocalDateTime.parse(response.dueOn, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        return Todo(
            id = response.id,
            title = response.title,
            isCompleted = response.status.toCompletionStatus(),
            createdAt = creationDate,
            updatedAt = creationDate,
            dueDate = dueDate,
        )
    }

    private fun String.toCompletionStatus() = when (this) {
        STATUS_COMPLETED -> true
        STATUS_PENDING -> false
        else -> throw IllegalArgumentException("$this status type is not supported")
    }

    private fun getRandomDateFromPast(): LocalDateTime {
        return LocalDateTime.now()
            .minusDays((0L..2L).random())
            .minusHours((0L..12L).random())
            .minusMinutes((0L..30L).random())
            .minusSeconds((0L..30L).random())
    }

}