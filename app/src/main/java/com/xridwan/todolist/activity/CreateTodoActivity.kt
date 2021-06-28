package com.xridwan.todolist.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.xridwan.todolist.databinding.ActivityCreateTodoBinding
import com.xridwan.todolist.model.Todo

class CreateTodoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCreateTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Create Todo"
        }

        binding.btnAdd.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val title = binding.etTitle.text.toString().trim()
        val desc = binding.etDesc.text.toString().trim()

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)) {
            Snackbar.make(v, "Field is empty", Snackbar.LENGTH_SHORT).show()

        } else {
            val ref = FirebaseDatabase.getInstance().getReference("Todos")
            val todoId = ref.push().key

            val todos = Todo(todoId, title, desc)

            if (todoId != null) {
                ref.child(todoId).setValue(todos).addOnCompleteListener {
                    Snackbar.make(v, "Success create todo", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}