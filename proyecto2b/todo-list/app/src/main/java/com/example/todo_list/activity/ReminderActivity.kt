package com.example.todo_list.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.models.TaskList
import com.example.todo_list.recyclers.TaskListRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList

class ReminderActivity : AppCompatActivity() {
    private val helper = FirestoreHelper()
    private var selectedTaskListCode: String? = null
    private var recyclerViewAdapter: TaskListRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        // Getting views references
        val btnAddTask = findViewById<ImageButton>(R.id.btn_add_task)
        val flAddList = findViewById<FrameLayout>(R.id.fl_add_list)
        val flTodayList = findViewById<FrameLayout>(R.id.fl_today)
        val flScheduledList = findViewById<FrameLayout>(R.id.fl_scheduled)
        val tvTodayNumber = findViewById<TextView>(R.id.today_number_txt)
        val tvScheduledNumber = findViewById<TextView>(R.id.scheduled_number_txt)
        val tvAllNumber = findViewById<TextView>(R.id.all_number_txt)
        val tvCompletedNumber = findViewById<TextView>(R.id.completed_number_txt)
        val flAllList = findViewById<FrameLayout>(R.id.fl_all)
        val flCompletedList = findViewById<FrameLayout>(R.id.fl_completed)
        val rvList = findViewById<RecyclerView>(R.id.rv_lists)

        // Setting actions
        btnAddTask.setOnClickListener { goToActivity(NewTaskActivity::class.java) }
        flAddList.setOnClickListener { goToActivity(NewListActivity::class.java) }
        flTodayList.setOnClickListener { goToTaskActivityWithParameters("Today") }
        flScheduledList.setOnClickListener { goToTaskActivityWithParameters("Scheduled") }
        flAllList.setOnClickListener { goToTaskActivityWithParameters("All") }
        flCompletedList.setOnClickListener { goToTaskActivityWithParameters("Completed") }

        // Setting the recycler view
        helper.getAllLists { taskLists ->
            initializeRecyclerView(ArrayList(taskLists!!.toMutableList()), rvList)
            registerForContextMenu(rvList)
        }

        // Setting reminders numbers
        setRemindersNumbers(tvTodayNumber, tvScheduledNumber, tvAllNumber, tvCompletedNumber)
    }

    private fun goToActivity(
        myClass: Class<*>
    ) {
        val intent = Intent(this, myClass)
        startActivity(intent)
    }

    fun goToTaskActivityWithParameters(listName: String, listCode: String? = null) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra("listName", listName)
        intent.putExtra("listCode", listCode)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_edit -> {
                val intent = Intent(this, NewListActivity::class.java)
                intent.putExtra("listCode", selectedTaskListCode)
                startActivity(intent)
                return true
            }

            R.id.mi_delete -> {
                helper.deleteList(selectedTaskListCode!!) { result ->
                    if (result) {
                        helper.getAllLists {  lists ->
                            recyclerViewAdapter!!.updateData(ArrayList(lists!!.toMutableList()))
                            recyclerViewAdapter!!.notifyDataSetChanged()
                        }
                    }
                }
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeRecyclerView(
        list: ArrayList<TaskList>,
        recyclerView: RecyclerView
    ) {
        val adapter = TaskListRecyclerViewAdapter(this, list)
        recyclerViewAdapter = adapter

        recyclerView.adapter = adapter
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

    private fun setRemindersNumbers(
        tvToday: TextView, tvScheduled: TextView, tvAll: TextView, tvCompleted: TextView
    ) {
        helper.getAllTasks { tasks ->
            val myTasks = ArrayList(tasks.toMutableList())

            // All number
            tvAll.text = myTasks.size.toString()

            // Today number
            val currentDate = Date() // Current date and time

            val todayTasks = ArrayList(myTasks.filter { task ->
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

                    currentDay == taskDay && currentMonth == taskMonth && currentYear == taskYear
                }
                else {
                    false
                }

            }.toMutableList())

            tvToday.text = todayTasks.size.toString()


            // Scheduled number
            val scheduledTasks = ArrayList(myTasks.filter { task ->
                Log.d(TAG, task.deadline.toString())
                task.deadline != null
            }.toMutableList())

            tvScheduled.text = scheduledTasks.size.toString()

            // Completed number
            val completedTasks = ArrayList(myTasks.filter { task ->
                task.status == "complete"
            }.toMutableList())

            tvCompleted.text = completedTasks.size.toString()
        }
    }

    fun setSelectedTaskListCode(listCode: String?) {
        selectedTaskListCode = listCode
    }
}