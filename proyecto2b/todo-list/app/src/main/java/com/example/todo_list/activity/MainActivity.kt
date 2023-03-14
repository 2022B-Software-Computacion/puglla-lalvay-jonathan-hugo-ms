package com.example.todo_list.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.todo_list.R
import com.example.todo_list.models.TaskList

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goToActivity(ReminderActivity::class.java)
    }

    private fun goToActivity(
        myClass: Class<*>
    ) {
        val intent = Intent(this, myClass)
        startActivity(intent)
    }
}