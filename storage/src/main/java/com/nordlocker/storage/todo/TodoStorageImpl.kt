package com.nordlocker.storage.todo

import com.nordlocker.domain.interfaces.TodoStorageService
import com.nordlocker.domain.models.Todo
import com.nordlocker.storage.mapper.TodoEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// TODO: create test
internal class TodoStorageImpl(
    database: TodoDatabase,
    private val todoEntityMapper: TodoEntityMapper,
) : TodoStorageService {

    private val dao = database.todoDao()

    override suspend fun updateOrCreate(list: List<Todo>) {
        dao.updateOrCreate(list.map(todoEntityMapper::mapToEntity))
    }

    override suspend fun getAll(): List<Todo> =
        dao.getAll().map(todoEntityMapper::mapToTodoModel)

    override fun observeAll(): Flow<List<Todo>> {
        return dao.observeAll().map { it.map(todoEntityMapper::mapToTodoModel) }
    }

    override suspend fun getById(id: Int): Todo {
        return todoEntityMapper.mapToTodoModel(dao.getById(id))
    }
}