package com.example.todo_list.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.models.Task
import com.example.todo_list.models.TaskList
import com.example.todo_list.recyclers.TaskRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class TaskActivity : AppCompatActivity() {
    private val helper = FirestoreHelper()
    private var taskList: TaskList? = null
    private var listName: String? = null
    private var recyclerViewAdapter: TaskRecyclerViewAdapter? = null
    private var myTasks: ArrayList<Task>? = null
    private var selectedTaskCode: String? = null
    private var selectedListCode: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        // Getting views references
        val btnBack = findViewById<TextView>(R.id.cancelList_title_txt)
        val tvLayoutTitle = findViewById<TextView>(R.id.task_title_txt)
        val rvTasks = findViewById<RecyclerView>(R.id.rv_tasks)

        // Getting intent values
        listName = intent.getStringExtra("listName")

        if (intent.getStringExtra("listCode") != null) {
            helper.getListByCode(intent.getStringExtra("listCode")!!) { list ->
                taskList = list

                // Getting all the tasks that are in a specific list
                helper.getTasksByListCode(taskList!!.listCode!!) { tasks ->
                    myTasks = ArrayList(tasks.toMutableList())

                    initializeRecyclerView(myTasks!!, rvTasks!!)
                    registerForContextMenu(rvTasks)
                }
            }
        }
        else {
            helper.getAllTasks { tasks ->
                Log.d(TAG, tasks.size.toString())
                myTasks = ArrayList(tasks.toMutableList())

                myTasks = filterTasks(listName!!, tasks)

                initializeRecyclerView(myTasks!!, rvTasks!!)
                registerForContextMenu(rvTasks)
            }
        }

        // Setting actions
        btnBack.setOnClickListener { goToActivity(ReminderActivity::class.java) }

        // Setting intent values
        tvLayoutTitle.text = "$listName"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_edit -> {
                val intent = Intent(this, NewTaskActivity::class.java)
                intent.putExtra("listName", listName)
                intent.putExtra("listCode", selectedListCode)
                intent.putExtra("taskCode", selectedTaskCode)
                startActivity(intent)
                return true
            }

            R.id.mi_delete -> {
                helper.deleteTask(selectedTaskCode!!, selectedListCode!!) { isSuccess ->
                    if (isSuccess) {
                        helper.getAllTasks { tasks ->
                            recyclerViewAdapter!!.updateData(filterTasks(listName!!, tasks))
                            recyclerViewAdapter!!.notifyDataSetChanged()
                        }
                    }
                }
                return true
            }

            R.id.mi_complete -> {
                // Mark selected task as completed
                helper.getTaskById(selectedListCode!!, selectedTaskCode!!) { task ->
                    task!!.status = "complete"

                    helper.updateTask(task) { isSuccess ->
                        if (isSuccess) {
                            helper.getAllTasks { tasks ->
                                recyclerViewAdapter!!.updateData(filterTasks(listName!!, tasks))
                                recyclerViewAdapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun goToActivity(
        myClass: Class<*>
    ) {
        val intent = Intent(this, myClass)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeRecyclerView(
        list: ArrayList<Task>,
        recyclerView: RecyclerView
    ) {
        val adapter = TaskRecyclerViewAdapter(this, list)
        recyclerViewAdapter = adapter

        recyclerView.adapter = adapter
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

    private fun filterTasks(listName: String, tasks: List<Task>): ArrayList<Task> {
        when (listName) {
            "Today" -> {
                val currentDate = Date() // Current date and time

                return ArrayList(tasks.filter { task ->
                    val dateTask = task.deadline

                    if (dateTask != null) {
                        val currentCalendar = Calendar.getInstance().apply { time = currentDate }
                        val taskCalendar = Calendar.getInstance().apply { time = dateTask }

                        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
                        val currentMonth = currentCalendar.get(Calendar.MONTH) + 1
                        val currentYear = currentCalendar.get(Calendar.YEAR)

                        val taskDay = taskCalendar.get(Calendar.DAY_OF_MONTH)
                        val taskMonth = taskCalendar.get(Calendar.MONTH) + 1
                        val taskYear = taskCalendar.get(Calendar.YEAR)

                        currentDay == taskDay
                                && currentMonth == taskMonth
                                && currentYear == taskYear
                    }
                    else {
                        false
                    }
                }.toMutableList())
            }
            "Scheduled" -> {
                return ArrayList(tasks.filter { task ->
                    task.deadline != null
                }.toMutableList())
            }
            "Completed" -> {
                return ArrayList(tasks.filter { task ->
                    task.status == "complete"
                }.toMutableList())
            }
            else -> {
                return ArrayList(tasks.toMutableList())
            }
        }
    }

    fun setSelectedTaskCode(taskCode: String?) {
        selectedTaskCode = taskCode
    }

    fun setSelectedListCode(listCode: String?) {
        selectedListCode = listCode
    }
}