package com.android.todolist.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.todolist.MainActivity
import com.android.todolist.MainActivity.Companion.CREATE_NEW_TASK
import com.android.todolist.MainActivity.Companion.EDIT_TASK
import com.android.todolist.MainActivity.Companion.RESULT
import com.android.todolist.MainActivity.Companion.TASK_ID
import com.android.todolist.databinding.ActivityNewTaskBinding
import com.android.todolist.datasource.TaskDataSource
import com.android.todolist.format
import com.android.todolist.model.Task
import com.android.todolist.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewTaskBinding
    private var task_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        insertListeners()

        task_id = intent.getIntExtra(TASK_ID, 0)
        if(task_id != 0)
            populateForm()
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
            TaskDataSource.addTask(
                Task(
                    title = binding.tilTitle.text,
                    description = binding.tilDescription.text,
                    date = binding.tilDate.text,
                    time = binding.tilTime.text,
                    id = task_id
                )
            )
            Intent().apply {
                if(task_id == 0)
                    putExtra(RESULT, CREATE_NEW_TASK)
                else
                    putExtra(RESULT, EDIT_TASK)
                setResult(RESULT_OK, this)
            }

            finish()
        }
    }

    private fun populateForm() {
        TaskDataSource.findbyId(task_id)?.let {
            binding.tilTitle.text = it.title
            binding.tilDescription.text = it.description
            binding.tilDate.text = it.date
            binding.tilTime.text = it.time
        }
    }
}