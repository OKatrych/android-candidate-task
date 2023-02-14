package com.nordlocker.android_task.ui.todo_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.nordlocker.android_task.databinding.TodoDetailsFragmentBinding
import com.nordlocker.android_task.ui.todo_details.TodoDetailsViewModel.TodoState.Error
import com.nordlocker.android_task.ui.todo_details.TodoDetailsViewModel.TodoState.Loaded
import com.nordlocker.android_task.ui.todo_details.TodoDetailsViewModel.TodoState.Loading
import com.nordlocker.domain.models.Todo
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TodoDetailsFragment : Fragment() {

    private val args: TodoDetailsFragmentArgs by navArgs()
    private val viewModel: TodoDetailsViewModel by viewModel {
        parametersOf(args.todoId)
    }

    private var _binding: TodoDetailsFragmentBinding? = null
    private val binding: TodoDetailsFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = TodoDetailsFragmentBinding.inflate(
        LayoutInflater.from(requireContext()),
        container,
        false
    ).apply { _binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.isCompleted.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setCompleted(isChecked)
        }
        binding.todoText.doAfterTextChanged { text ->
            text?.let { viewModel.onTodoTitleChange(it.toString()) }
        }
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.todoState.collect { todoState ->
                    when (todoState) {
                        is Loaded -> renderTodo(todoState.todo)
                        is Error -> {
                            Toast.makeText(
                                requireContext().applicationContext,
                                "Error while loading todo: ${todoState.error.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is Loading -> Unit // No-op
                    }
                }
            }
        }
    }

    private fun renderTodo(todo: Todo) {
        binding.todoText.apply {
            if (todo.title != text.toString()) {
                setText(todo.title)
            }
        }
        binding.isCompleted.isChecked = todo.isCompleted
    }
}