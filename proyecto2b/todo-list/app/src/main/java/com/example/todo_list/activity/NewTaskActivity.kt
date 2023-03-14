package com.example.todo_list.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.todo_list.R
import com.example.todo_list.models.Task
import com.example.todo_list.models.TaskList
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class NewTaskActivity : AppCompatActivity() {
    private val helper = FirestoreHelper()
    private var taskLists: List<TaskList>? = null
    private var listCode: String? = null
    private var taskCode: String? = null
    private var tvDate: TextView? = null
    private var tvTime: TextView? = null
    private var inputTaskTitle: TextInputEditText? = null
    private var inputTaskNote: TextInputEditText? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        // Getting views references
        val btnCancel = findViewById<TextView>(R.id.cancelList_title_txt)
        val btnAddTask = findViewById<TextView>(R.id.addList_title_txt)
        val layoutTitle = findViewById<TextView>(R.id.newNote_title_txt)
        val flAddDate = findViewById<FrameLayout>(R.id.fl_add_date)
        val flAddTime = findViewById<FrameLayout>(R.id.fl_add_time)
        val flTaskList = findViewById<FrameLayout>(R.id.fl_note_details)
        val imgListColor = findViewById<ImageView>(R.id.img_list_color)
        val tvListName = findViewById<TextView>(R.id.tv_list_name)
        val popupMenu = PopupMenu(this, flTaskList)
        tvDate = findViewById(R.id.tv_date)
        tvTime = findViewById(R.id.tv_time)
        inputTaskTitle = findViewById(R.id.input_note_title)
        inputTaskNote = findViewById(R.id.input_note_content)

        // Getting possible intents
        if (intent.hasExtra("listCode") && intent.hasExtra("taskCode")) {
            listCode = intent.getStringExtra("listCode")
            taskCode = intent.getStringExtra("taskCode")

            // Setting layout variations
            layoutTitle.text = "Update Task"
            btnAddTask.text = "Update"

            // Setting current task values
            helper.getTaskById(listCode!!, taskCode!!) { task ->
                inputTaskTitle!!.setText(task!!.title)
                inputTaskNote!!.setText(task.notes)

                tvDate!!.text = getTaskDate(task.deadline)
                tvTime!!.text = getTaskTime(task.deadline)

                helper.getListByCode(listCode!!) { list ->
                    tvListName.text = list!!.name
                    setSelectedTaskList(list.name!!, tvListName, imgListColor)
                }
            }
        }

        // Setting actions
        btnCancel.setOnClickListener {
            if (listCode != null) {
                helper.getListByCode(listCode!!) { list ->
                    var listName = list!!.name
                    val lastListName = intent.getStringExtra("listName")
                    val newIntent = Intent(this, TaskActivity::class.java)

                    if (lastListName == "Today" || lastListName == "Scheduled" ||
                        lastListName == "Completed" || lastListName == "All") {
                        listName = lastListName
                        listCode = null
                    }

                    newIntent.putExtra("listName", listName)
                    newIntent.putExtra("listCode", listCode)
                    startActivity(newIntent)
                }
            }
            else {
                goToActivity(ReminderActivity::class.java)
            }
        }
        flAddDate.setOnClickListener { showDatePickerDialog() }
        flAddTime.setOnClickListener { showTimePickerDialog() }

        // Setting task list options
        helper.getAllLists { lists ->
            taskLists = lists

            for (taskList in taskLists!!) {
                popupMenu.menu.add(taskList.name)
            }
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            tvListName.text = menuItem.title
            setSelectedTaskList(menuItem.title!!, tvListName, imgListColor)
            true
        }

        flTaskList.setOnClickListener { popupMenu.show() }

        btnAddTask.setOnClickListener{
            if (intent.hasExtra("listCode") && intent.hasExtra("taskCode")) {
                setUpdateTaskOnFirestore(tvListName.text.toString(), intent)
            }
            else {
                val taskListName = tvListName.text.toString()
                setAddTaskOnFirestore(taskListName)
            }
        }
    }

    private fun setAddTaskOnFirestore(taskListName: String) {
        helper.getListByName(taskListName) { taskList ->
            val taskListCode = taskList!!.listCode
            val dateTime = tvDate!!.text.toString() + "T" + tvTime!!.text.toString()

            val newTask = Task(
                title = inputTaskTitle?.text.toString(),
                notes = inputTaskNote?.text.toString(),
                listCode = taskListCode,
                status = "incomplete",
                deadline = getDeadline(dateTime)
            )

            helper.createTask(newTask) { taskCode ->
                if(!isAValidTaskCode(taskCode)) {
                    Log.e("NewTaskActivity", "Error creating task!")
                } else {
                    newTask.taskCode = taskCode
                    goToActivity(ReminderActivity::class.java)
                }
            }
        }
    }

    private fun setUpdateTaskOnFirestore(taskListName: String, currentIntent: Intent) {
        helper.getTaskById(listCode!!, taskCode!!) { task->
            val dateTime = tvDate!!.text.toString() + "T" + tvTime!!.text.toString()

            helper.getListByName(taskListName) { list ->
                val updatedTask = Task(
                    title = inputTaskTitle?.text.toString(),
                    notes = inputTaskNote?.text.toString(),
                    listCode = list!!.listCode,
                    status = task!!.status,
                    deadline = getDeadline(dateTime)
                )

                // Checking if the list was changed
                if (listCode != updatedTask.listCode) {
                    helper.createTask(updatedTask) { newTaskCode ->
                        updatedTask.taskCode = newTaskCode

                        helper.deleteTask(taskCode!!, listCode!!) {
                            var listName: String = list.name!!
                            val lastListName = currentIntent.getStringExtra("listName")
                            var lastListCode = updatedTask.listCode

                            if (lastListName == "Today" || lastListName == "Scheduled" ||
                                lastListName == "Completed" || lastListName == "All") {
                                listName = lastListName
                                lastListCode = null
                            }

                            val intent = Intent(this, TaskActivity::class.java)
                            intent.putExtra("listName", listName)
                            intent.putExtra("listCode", lastListCode)
                            startActivity(intent)
                        }
                    }
                }
                else {
                    helper.updateTask(updatedTask) {
                        var listName: String = list.name!!
                        val lastListName = currentIntent.getStringExtra("listName")
                        var lastListCode = updatedTask.listCode

                        if (lastListName == "Today" || lastListName == "Scheduled" ||
                            lastListName == "Completed" || lastListName == "All") {
                            listName = lastListName
                            lastListCode = null
                        }

                        val intent = Intent(this, TaskActivity::class.java)
                        intent.putExtra("listName", listName)
                        intent.putExtra("listCode", lastListCode)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDeadline(dateTime: String): Date? {
        if (dateTime != "DateTTime") {
            val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
            return dateTimeFormatter.parse(dateTime)
        }
        return null
    }

    private fun isAValidTaskCode(taskCode: String?): Boolean {
        if (taskCode == null) {
            return false
        }
        return true
    }

    private fun goToActivity(
        myClass: Class<*>
    ) {
        val intent = Intent(this, myClass)
        startActivity(intent)
    }

    private fun showDatePickerDialog() {
        // Get the current date for the DatePickerDialog
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, yearSelected, monthSelected, daySelected ->
                // Get the selected date from the DatePicker
                val selectedDate = "$yearSelected-${monthSelected + 1}-$daySelected"

                // Use the selected date
                tvDate!!.text = selectedDate
            },
            year,
            month,
            day
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        // Get the current time for the TimePickerDialog
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and set the initial time
        val timePickerDialog = TimePickerDialog(
            this, // Context
            { _, hourSelected, minuteSelected ->
                // Get the selected time from the TimePicker
                val selectedTime = "$hourSelected:$minuteSelected"

                // Set the selected time in a TextView
                tvTime!!.text = selectedTime
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun setSelectedTaskList(menuItemTitle: CharSequence, tv: TextView, img: ImageView) {
        var selectedTaskList: TaskList? = null
        var selectedImageColor = R.drawable.color_purple
        var selectedTextColor = R.color.purple_dark

        for (taskList in taskLists!!) {
            if (taskList.name == menuItemTitle) {
                selectedTaskList = taskList
                break
            }
        }

        when (selectedTaskList!!.hexColor) {
            "#FF2E63" -> {
                selectedImageColor = R.drawable.color_pink
                selectedTextColor = R.color.pink
            }
            "#08D9D6" -> {
                selectedImageColor = R.drawable.color_cyan
                selectedTextColor = R.color.cyan_dark
            }
            "#F38181" -> {
                selectedImageColor = R.drawable.color_orange
                selectedTextColor = R.color.orange_dark
            }
            "#6A2C70" -> {
                selectedImageColor = R.drawable.color_purple
                selectedTextColor = R.color.purple_dark
            }
            "#FFDE7D" -> {
                selectedImageColor = R.drawable.color_yellow
                selectedTextColor = R.color.yellow
            }
        }

        img.setBackgroundResource(selectedImageColor)
        tv.setTextColor(resources.getColor(selectedTextColor, null))
    }

    private fun getTaskDate(deadline: Date?): String {
        return if (deadline != null) {
            val taskCalendar = Calendar.getInstance().apply { time = deadline }

            val taskDay = taskCalendar.get(Calendar.DAY_OF_MONTH)
            val taskMonth = taskCalendar.get(Calendar.MONTH) + 1
            val taskYear = taskCalendar.get(Calendar.YEAR)

            "$taskYear-$taskMonth-$taskDay"
        } else {
            "Date"
        }
    }

    private fun getTaskTime(deadline: Date?): String {
        return if (deadline != null) {
            val taskCalendar = Calendar.getInstance().apply { time = deadline }

            val taskHour = taskCalendar.get(Calendar.HOUR_OF_DAY)
            val taskMinutes = taskCalendar.get(Calendar.MINUTE)

            "$taskHour:$taskMinutes"
        } else {
            "Time"
        }
    }
}