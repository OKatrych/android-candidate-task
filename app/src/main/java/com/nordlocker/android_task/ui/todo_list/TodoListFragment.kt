package com.nordlocker.android_task.ui.todo_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.nordlocker.android_task.databinding.TodoListFragmentBinding
import com.nordlocker.domain.models.Todo
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TodoListFragment : Fragment() {

    private val viewModel: TodoListViewModel by viewModel()

    private var _binding: TodoListFragmentBinding? = null
    private val binding: TodoListFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = TodoListFragmentBinding.inflate(
        LayoutInflater.from(requireContext()),
        container,
        false
    ).apply { _binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.todoList.apply {
            adapter = GroupieAdapter().apply {
                setOnItemClickListener { item, _ ->
                    if (item is TodoListItem) {
                        navigateToDetailsFragment(item.todo)
                    }
                }
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.sortButton.setOnClickListener {
            viewModel.changeSortingType()
        }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.todoState.collect { todos ->
                    Timber.d("Loaded todos: $todos")
                    updateTodoList(todos)
                }
            }
        }
    }

    private fun updateTodoList(todos: List<Todo>) {
        val adapter = binding.todoList.adapter as GroupieAdapter
        adapter.update(todos.map(::TodoListItem))
    }

    private fun navigateToDetailsFragment(todo: Todo) {
        findNavController().navigate(TodoListFragmentDirections.openDetails(todo.id))
    }
}