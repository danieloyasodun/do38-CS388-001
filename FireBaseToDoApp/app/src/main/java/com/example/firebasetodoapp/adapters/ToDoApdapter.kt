package com.example.firebasetodoapp.adapters

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetodoapp.databinding.ItemTodoBinding
import com.example.firebasetodoapp.models.Todo

class TodoAdapter(
    private val todos: List<Todo>,
    private val onTodoClick: (Todo) -> Unit // Click listener as a lambda
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.contentTextView.text = todo.content
            binding.root.setOnClickListener { onTodoClick(todo) } // Set click listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount(): Int = todos.size
}