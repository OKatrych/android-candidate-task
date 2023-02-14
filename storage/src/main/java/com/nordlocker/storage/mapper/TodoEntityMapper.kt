package com.nordlocker.storage.mapper

import com.nordlocker.domain.models.Todo
import com.nordlocker.storage.todo.TodoEntity

internal class TodoEntityMapper {

    fun mapToEntity(todo: Todo): TodoEntity {
        return TodoEntity(
            id = todo.id,
            title = todo.title,
            isCompleted = todo.isCompleted,
            createdAt = todo.createdAt,
            updatedAt = todo.updatedAt,
            dueDate = todo.dueDate,
        )
    }

    fun mapToTodoModel(todoEntity: TodoEntity): Todo {
        return Todo(
            id = todoEntity.id,
            title = todoEntity.title,
            isCompleted = todoEntity.isCompleted,
            createdAt = todoEntity.createdAt,
            updatedAt = todoEntity.updatedAt,
            dueDate = todoEntity.dueDate,
        )
    }
}
