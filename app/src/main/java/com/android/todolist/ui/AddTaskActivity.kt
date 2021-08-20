package com.android.todolist.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.todolist.R
import com.android.todolist.ui.MainActivity.Companion.CREATE_NEW_TASK
import com.android.todolist.ui.MainActivity.Companion.EDIT_TASK
import com.android.todolist.ui.MainActivity.Companion.RESULT
import com.android.todolist.databinding.ActivityNewTaskBinding
import com.android.todolist.format
import com.android.todolist.model.Task
import com.android.todolist.repository.TaskRepository
import com.android.todolist.text
import com.android.todolist.ui.MainActivity.Companion.TASK
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewTaskBinding
    private val taskRepository by lazy {
        TaskRepository(this)
    }
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        insertListeners()

        task = intent.getParcelableExtra(TASK)
        task?.let {
            populateForm(it)
            binding.btnSaveTask.text = getString(R.string.save_task_label)
        }
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datepicker = MaterialDatePicker.Builder.datePicker().build()
            datepicker.show(supportFragmentManager, "DATE_PICKER")
            datepicker.addOnPositiveButtonClickListener {
                val offset = TimeZone.getDefault().getOffset(Date().time) * -1
                val date = Date(it + offset)
                binding.tilDate.text = date.format()
            }
        }

        binding.tilTime.editText?.setOnClickListener {
            val timepicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .build()
            timepicker.show(supportFragmentManager, null)
            timepicker.addOnPositiveButtonClickListener {
                val hour = if (timepicker.hour < 10) "0${timepicker.hour}" else timepicker.hour
                val minute = if (timepicker.minute < 10) "0${timepicker.minute}" else timepicker.minute
                binding.tilTime.text = "$hour:$minute"
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSaveTask.setOnClickListener {
            runBlocking {
                withContext(Dispatchers.IO) {
                    if(task == null)
                        taskRepository.addTask(
                            Task(
                                title = binding.tilTitle.text,
                                description = binding.tilDescription.text,
                                date = binding.tilDate.text,
                                time = binding.tilTime.text
                            )
                        )
                    else
                        taskRepository.updateTask(
                            Task(
                                title = binding.tilTitle.text,
                                description = binding.tilDescription.text,
                                date = binding.tilDate.text,
                                time = binding.tilTime.text,
                                id = task?.id ?: 0
                            )
                        )
                }
            }

            Intent().apply {
                if(task == null)
                    putExtra(RESULT, CREATE_NEW_TASK)
                else
                    putExtra(RESULT, EDIT_TASK)
                setResult(RESULT_OK, this)
            }

            finish()
        }

        binding.tlTitle.setNavigationOnClickListener {
            finish()
        }
    }

    private fun populateForm(task: Task) {
            binding.tilTitle.text = task.title
            binding.tilDescription.text = task.description
            binding.tilDate.text = task.date
            binding.tilTime.text = task.time
    }
}