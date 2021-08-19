package com.android.todolist.repository

import android.content.Context
import com.android.todolist.datasource.database.Database
import com.android.todolist.model.Task

class TaskRepository(private val context: Context) {

    private val taskDao by lazy {
        Database.instance(context).taskDao()
    }

    suspend fun getTasks(): List<Task> {
        return taskDao.getTasks()
    }

    suspend fun addTask(tasks: Task) {
        taskDao.addTask(tasks)
    }

    suspend fun updateTask(task: Task) {
        taskDao.editTask(task)
    }

    suspend fun deleteTask(tasks: List<Task>) {
        taskDao.deleteTask(tasks)
    }
}