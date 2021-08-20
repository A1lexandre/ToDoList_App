package com.android.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.todolist.databinding.FragmentTaskDetailBinding
import com.android.todolist.model.Task
import com.android.todolist.ui.MainActivity.Companion.TASK
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TaskDetailFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTaskDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getParcelable<Task>(TASK)?.let {
            binding.tvTtitle.text = if(it.title == "") "Não informado" else it.title
            binding.tvTdesc.text = if(it.description == "") "Não informado" else it.description
            binding.tvTdate.text = if(it.date == "") "Não informado" else it.date
            binding.tvTtime.text = if(it.time == "") "Não informado" else it.time
        }
    }
}