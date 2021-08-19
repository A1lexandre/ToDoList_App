package com.android.todolist.datasource

import com.android.todolist.model.Task

object TaskDataSource {

    private val list = arrayListOf<Task>()

    fun getList() = list.toList().sortedBy { it.id }

    fun addTask(task: Task) {
        if(task.id == 0)
            list.add(task.copy(id = list.size + 1))
        else {
            list.remove(task)
            list.add(task)
        }
    }

    fun findbyId(id: Int) = list.find { it.id == id }

    fun deleteTask(task: Task) {
        list.remove(task)
    }

}