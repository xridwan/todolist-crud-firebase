package com.xridwan.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xridwan.todolist.R
import com.xridwan.todolist.databinding.TodoItemLayoutBinding
import com.xridwan.todolist.model.SubTodo

class SubTodoAdapter : RecyclerView.Adapter<SubTodoAdapter.SubTodoViewHolder>() {
    private val subTodoList = arrayListOf<SubTodo>()

    fun setData(list: MutableList<SubTodo>) {
        subTodoList.clear()
        subTodoList.addAll(list)
        notifyDataSetChanged()
    }

    inner class SubTodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TodoItemLayoutBinding.bind(itemView)
        fun bind(subTodo: SubTodo) {
            with(binding) {
                tvTitle.text = subTodo.subTitle
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTodoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.todo_item_layout, parent, false)
        return SubTodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubTodoViewHolder, position: Int) {
        holder.bind(subTodoList[position])
    }

    override fun getItemCount(): Int = subTodoList.size
}