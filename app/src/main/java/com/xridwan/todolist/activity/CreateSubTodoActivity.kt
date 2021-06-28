package com.xridwan.todolist.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.xridwan.todolist.adapter.SubTodoAdapter
import com.xridwan.todolist.databinding.ActivityCreateSubTodoBinding
import com.xridwan.todolist.model.SubTodo

class CreateSubTodoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCreateSubTodoBinding
    private lateinit var adapter: SubTodoAdapter
    private lateinit var ref: DatabaseReference

    private lateinit var todolist: MutableList<SubTodo>

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSubTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Create SubTodo"
        }

        todolist = mutableListOf()

        val id = intent.getStringExtra(EXTRA_ID)
        val title = intent.getStringExtra(EXTRA_TITLE)

        recyclerView()

        ref = FirebaseDatabase.getInstance().getReference("SubTodos").child(id.toString())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    todolist.clear()
                    for (ds in snapshot.children) {
                        val subTodo = ds.getValue(SubTodo::class.java)
                        if (subTodo != null) {
                            todolist.add(subTodo)
                            adapter.setData(todolist)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CreateSubTodoActivity, "" + error.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })

        binding.tvTitle.text = title
        binding.btnAdd.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val subTitle = binding.etTitle.text.toString().trim()
        val subDesc = binding.etDesc.text.toString().trim()

        if (TextUtils.isEmpty(subTitle) || TextUtils.isEmpty(subDesc)) {
            Snackbar.make(v, "Field is empty", Snackbar.LENGTH_SHORT).show()

        } else {
            val subTodoId = ref.push().key

            val subTodos = SubTodo(subTodoId, subTitle, subDesc)

            if (subTodoId != null) {
                ref.child(subTodoId).setValue(subTodos).addOnCompleteListener {
                    Snackbar.make(v, "Success create subtodo", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun recyclerView() {
        adapter = SubTodoAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvSubtodo.layoutManager = LinearLayoutManager(this@CreateSubTodoActivity)
            rvSubtodo.setHasFixedSize(true)
            rvSubtodo.adapter = adapter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}