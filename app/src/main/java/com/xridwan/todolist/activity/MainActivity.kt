package com.xridwan.todolist.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.xridwan.todolist.adapter.TodoAdapter
import com.xridwan.todolist.databinding.ActivityMainBinding
import com.xridwan.todolist.model.Todo
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var ref: DatabaseReference
    private lateinit var adapter: TodoAdapter
    private lateinit var todolist: MutableList<Todo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todolist = mutableListOf()

        recyclerView()

        ref = FirebaseDatabase.getInstance().getReference("Todos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    todolist.clear()
                    for (ds in snapshot.children) {
                        val todo = ds.getValue(Todo::class.java)
                        if (todo != null) {
                            todolist.add(todo)
                            adapter.setData(todolist)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "" + error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.fabCreate.setOnClickListener {
            startActivity(Intent(this, CreateTodoActivity::class.java))
        }
    }

    private fun recyclerView() {
        adapter = TodoAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvTodos.layoutManager = LinearLayoutManager(this@MainActivity)
            rvTodos.setHasFixedSize(true)
            rvTodos.adapter = adapter
        }
    }
}