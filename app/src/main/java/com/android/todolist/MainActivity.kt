package com.android.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.android.todolist.databinding.ActivityMainBinding
import com.android.todolist.datasource.TaskDataSource
import com.android.todolist.ui.AddTaskActivity
import com.android.todolist.ui.TaskAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var register: ActivityResultLauncher<Intent>
    private val taskAdapter by lazy {
        TaskAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvTasks.adapter = taskAdapter
        updateList()
        insertListeners()
        insertRegister()
    }

    private fun insertListeners() {
        binding.btnAddTask.setOnClickListener {
            register.launch(Intent(this, AddTaskActivity::class.java))
        }

        taskAdapter.editListener =  {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(TASK_ID, it.id)
            register.launch(intent)
        }

        taskAdapter.deleteListener = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        Toast.makeText(this, list.toString(), Toast.LENGTH_SHORT).show()
        taskAdapter.submitList(list)
        binding.ltEmptyState.emptyStateRoot.visibility =
            if(list.isEmpty())
                View.VISIBLE
            else
                View.GONE

    }

    private fun insertRegister() {
        register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                result.data?.let {
                    if(it.hasExtra(RESULT) &&
                            (it.getIntExtra(RESULT, 0) == CREATE_NEW_TASK
                                    || it.getIntExtra(RESULT, 0) == EDIT_TASK))
                        updateList()

                }
            }
        }
    }

    companion object {
        const val CREATE_NEW_TASK = 1
        const val EDIT_TASK = 2
        const val RESULT = "result"
        const val TASK_ID = "id"
    }

    fun showList(view: View) {
        Toast.makeText(this, TaskDataSource.getList().toString(), Toast.LENGTH_SHORT).show()
        updateList()
    }
}