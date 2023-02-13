package com.nordlocker.android_task.ui.todo_list

import android.graphics.Paint
import android.view.View
import com.nordlocker.android_task.R
import com.nordlocker.android_task.databinding.TodoItemBinding
import com.nordlocker.domain.models.Todo
import com.xwray.groupie.viewbinding.BindableItem
import java.time.format.DateTimeFormatter

data class TodoListItem(
    val todo: Todo,
) : BindableItem<TodoItemBinding>() {

    companion object {
        private const val DATE_PATTERN = "yyyy-mm-dd HH:mm:ss"
    }

    override fun bind(viewBinding: TodoItemBinding, position: Int) {
        viewBinding.itemTitleTextView.apply {
            text = todo.title
            paintFlags = if (todo.isCompleted) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        viewBinding.updateTime.text =
            todo.updatedAt.format(DateTimeFormatter.ofPattern(DATE_PATTERN))
    }

    override fun getId() = todo.id.toLong()
    override fun getLayout() = R.layout.todo_item
    override fun initializeViewBinding(view: View) = TodoItemBinding.bind(view)
}