package com.android.todolist.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.todolist.datasource.database.dao.TaskDao
import com.android.todolist.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

object Database {

    @Volatile
    private lateinit var database: AppDatabase

    fun instance(context: Context): AppDatabase {
        synchronized(this) {
            if(::database.isInitialized) return database
            database = Room.databaseBuilder(context,
                    AppDatabase::class.java,
                    "todolist-db")
                    .build()
            return database
        }
    }
}