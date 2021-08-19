package com.android.todolist.datasource.database.dao

import androidx.room.*
import com.android.todolist.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * from task")
    suspend fun getTasks(): List<Task>

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun editTask(task: Task)

    @Delete
    suspend fun deleteTask(tasks: List<Task>)
}