package com.android.todolist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.android.todolist.databinding.ActivityMainBinding
import com.android.todolist.datasource.TaskDataSource
import com.android.todolist.model.Task
import com.android.todolist.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var register: ActivityResultLauncher<Intent>
    private val taskRepository by lazy {
        TaskRepository(this)
    }
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
            intent.putExtra(TASK, it)
            register.launch(intent)
        }

        taskAdapter.deleteListener = {
            runBlocking {
                taskRepository.deleteTask(listOf(it))
                runOnUiThread {
                    updateList()
                }
            }
        }
    }

    private fun updateList() {
        var list: List<Task>
        runBlocking {
            withContext(Dispatchers.IO) {
                list = taskRepository.getTasks()
            }
            runOnUiThread {
                taskAdapter.submitList(list)
                binding.ltEmptyState.emptyStateRoot.visibility =
                    if(list.isEmpty())
                        View.VISIBLE
                    else
                        View.GONE

            }
        }
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
        const val TASK = "task"
    }

}