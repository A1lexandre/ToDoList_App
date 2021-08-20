package com.android.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.todolist.R
import com.android.todolist.databinding.TaskListItemBinding
import com.android.todolist.model.Task

class TaskAdapter: ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    var editListener: (Task) -> Unit = {}
    var deleteListener: (Task) -> Unit = {}
    var showTaskDetailClick: (Task) -> Unit = {}

    inner class TaskViewHolder(private val binding: TaskListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            task.let {
                binding.tvTaskTitle.text = it.title
                binding.tvTaskTime.text = "${it.date} ${it.time}"
                binding.manageTaskMenu.setOnClickListener {
                    showPopup(task)
                }
                binding.root.setOnClickListener {
                    showTaskDetailClick(task)
                }
            }
        }

        private fun showPopup(taskItem: Task) {
            val menuButton = binding.manageTaskMenu
            val popupMenu = PopupMenu(menuButton.context, menuButton)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.action_edit -> {
                        editListener(taskItem)
                    }
                    R.id.action_delete -> {
                        deleteListener(taskItem)
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffCallback: DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        oldItem.let { t1 ->
            newItem.let { t2->
                return t1.id == t2.id &&
                        t1.title == t2.title &&
                        t1.description == t2.description &&
                        t1.date == t2.date &&
                        t1.time == t2.time
            }
        }
    }
}