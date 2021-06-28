package com.xridwan.todolist.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.xridwan.todolist.R
import com.xridwan.todolist.activity.CreateSubTodoActivity
import com.xridwan.todolist.databinding.TodoItemLayoutBinding
import com.xridwan.todolist.databinding.UpdateTodoLayoutBinding
import com.xridwan.todolist.model.Todo

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private val todolist = arrayListOf<Todo>()

    fun setData(list: MutableList<Todo>) {
        todolist.clear()
        todolist.addAll(list)
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TodoItemLayoutBinding.bind(itemView)
        fun bind(todo: Todo) {
            with(binding) {
                tvTitle.text = todo.title

                itemView.setOnLongClickListener {
                    showDialog(itemView.context, todo)
                    return@setOnLongClickListener true
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, CreateSubTodoActivity::class.java)
                    intent.putExtra(CreateSubTodoActivity.EXTRA_ID, todo.id)
                    intent.putExtra(CreateSubTodoActivity.EXTRA_TITLE, todo.title)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.todo_item_layout, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todolist[position])
    }

    override fun getItemCount(): Int = todolist.size

    private fun showDialog(context: Context, todo: Todo) {
        val builder = AlertDialog.Builder(context)
        val binding = UpdateTodoLayoutBinding.inflate(LayoutInflater.from(context))

        builder.setTitle("Confirmation")
        builder.setView(binding.root)

        binding.etTitle.setText(todo.title)
        binding.etDesc.setText(todo.desc)

        builder.setPositiveButton("Update") { _: DialogInterface, _: Int ->
            val ref = FirebaseDatabase.getInstance().getReference("Todos")

            val title = binding.etTitle.text.toString().trim()
            val desc = binding.etDesc.text.toString().trim()

            val todos = Todo(todo.id, title, desc)

            ref.child(todo.id.toString()).setValue(todos)

            Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Delete") { _: DialogInterface, _: Int ->
            val ref = FirebaseDatabase.getInstance().getReference("Todos").child(todo.id.toString())
            val reference =
                FirebaseDatabase.getInstance().getReference("SubTodo").child(todo.id.toString())

            ref.removeValue()
            reference.removeValue()

            Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show()
        }

        val alert = builder.create()
        alert.show()
    }
}
