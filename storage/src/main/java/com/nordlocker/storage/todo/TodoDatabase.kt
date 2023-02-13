package com.nordlocker.storage.todo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nordlocker.storage.util.DateTypeConverter

@Database(version = 1, entities = [TodoEntity::class])
@TypeConverters(DateTypeConverter::class)
internal abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}