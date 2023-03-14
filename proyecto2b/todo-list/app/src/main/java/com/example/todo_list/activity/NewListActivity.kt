package com.example.todo_list.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.todo_list.R
import com.example.todo_list.models.TaskList
import com.google.android.material.textfield.TextInputEditText

class NewListActivity : AppCompatActivity() {
    private val helper = FirestoreHelper()
    private var selectedHexColor: String = "#FF2E63"
    private var selectedIconPath: String = "home"

    @SuppressLint("DiscouragedApi", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_list)

        // Getting views references
        val btnCancel = findViewById<TextView>(R.id.cancelList_title_txt)
        val btnAdd = findViewById<TextView>(R.id.addList_title_txt)
        val tvLayoutTitle = findViewById<TextView>(R.id.newList_title_txt)

        val inputListName = findViewById<TextInputEditText>(R.id.input_list_name)
        val inputListDescription = findViewById<TextInputEditText>(R.id.input_list_description)

        val imgSelectedColor = findViewById<ImageView>(R.id.selected_color)
        val imgSelectedIcon = findViewById<ImageView>(R.id.selected_icon)

        val imgPinkColor = findViewById<ImageView>(R.id.color_pink)
        val imgPurpleColor = findViewById<ImageView>(R.id.color_purple)
        val imgOrangeColor = findViewById<ImageView>(R.id.color_orange)
        val imgCyanColor = findViewById<ImageView>(R.id.color_cyan)
        val imgYellowColor = findViewById<ImageView>(R.id.color_yellow)

        val imgHomeIcon = findViewById<ImageView>(R.id.home_icon)
        val imgSchoolIcon = findViewById<ImageView>(R.id.school_icon)
        val imgWorkIcon = findViewById<ImageView>(R.id.work_icon)
        val imgMedicalIcon = findViewById<ImageView>(R.id.medical_icon)
        val imgWarningIcon = findViewById<ImageView>(R.id.warning_icon)

        // Getting possible intents when an edition action is called
        if (intent.hasExtra("listCode")) {
            // Setting layout
            tvLayoutTitle.text = "Update List"
            btnAdd.text = "Update"

            // Setting list parameters
            helper.getListByCode(intent.getStringExtra("listCode")!!) { list ->
                inputListName.setText(list!!.name)
                inputListDescription.setText(list.description)

                selectedHexColor = list.hexColor!!
                selectedIconPath = list.iconPath!!

                setCurrentColor(imgSelectedColor)
                setCurrentIconPath(imgSelectedIcon)
            }
        }

        // Setting actions
        btnCancel.setOnClickListener { goToActivity(ReminderActivity::class.java) }

        btnAdd.setOnClickListener {
            val newList = TaskList(
                name = inputListName.text.toString(),
                description = inputListDescription.text.toString(),
                hexColor = selectedHexColor,
                iconPath = selectedIconPath
            )

            if (intent.hasExtra("listCode")) {
                newList.listCode = intent.getStringExtra("listCode")

                helper.updateList(newList.listCode!!, newList) {
                    goToActivity(ReminderActivity::class.java)
                }
            }
            else {
                helper.createList(newList) { listCode ->
                    newList.listCode = listCode
                    goToActivity(ReminderActivity::class.java)
                }
            }
        }

        imgPinkColor.setOnClickListener {
            setSelectedColor(imgSelectedColor, R.drawable.color_pink)
            selectedHexColor = "#FF2E63"
        }
        imgPurpleColor.setOnClickListener {
            setSelectedColor(imgSelectedColor, R.drawable.color_purple)
            selectedHexColor = "#6A2C70"
        }
        imgOrangeColor.setOnClickListener {
            setSelectedColor(imgSelectedColor, R.drawable.color_orange)
            selectedHexColor = "#F38181"
        }
        imgCyanColor.setOnClickListener {
            setSelectedColor(imgSelectedColor, R.drawable.color_cyan)
            selectedHexColor = "#08D9D6"
        }
        imgYellowColor.setOnClickListener {
            setSelectedColor(imgSelectedColor, R.drawable.color_yellow)
            selectedHexColor = "#FFDE7D"
        }

        imgHomeIcon.setOnClickListener {
            setSelectedIcon(imgSelectedIcon, R.drawable.home)
            selectedIconPath = "home"
        }
        imgSchoolIcon.setOnClickListener {
            setSelectedIcon(imgSelectedIcon, R.drawable.school)
            selectedIconPath = "school"
        }
        imgWorkIcon.setOnClickListener {
            setSelectedIcon(imgSelectedIcon, R.drawable.work)
            selectedIconPath = "work"
        }
        imgMedicalIcon.setOnClickListener {
            setSelectedIcon(imgSelectedIcon, R.drawable.health)
            selectedIconPath = "health"
        }
        imgWarningIcon.setOnClickListener {
            setSelectedIcon(imgSelectedIcon, R.drawable.warning)
            selectedIconPath = "warning"
        }
    }

    private fun goToActivity(
        myClass: Class<*>
    ) {
        val intent = Intent(this, myClass)
        startActivity(intent)
    }

    private fun setSelectedColor(imageViewColor: ImageView, selectedColor: Int) {
        imageViewColor.setImageResource(selectedColor)
    }

    private fun setSelectedIcon(imageViewIcon: ImageView, selectedIcon: Int) {
        imageViewIcon.setImageResource(selectedIcon)
    }

    private fun setCurrentColor(imageViewColor: ImageView) {
        var currentDrawableColorID: Int = R.drawable.color_pink

        when (selectedHexColor) {
            "#FF2E63" -> currentDrawableColorID = R.drawable.color_pink
            "#6A2C70" -> currentDrawableColorID = R.drawable.color_purple
            "#F38181" -> currentDrawableColorID = R.drawable.color_orange
            "#08D9D6" -> currentDrawableColorID = R.drawable.color_cyan
            "#FFDE7D" -> currentDrawableColorID = R.drawable.color_yellow
        }

        setSelectedColor(imageViewColor, currentDrawableColorID)
    }

    private fun setCurrentIconPath(imageViewColor: ImageView) {
        var currentDrawableIconID: Int = R.drawable.home

        when (selectedIconPath) {
            "home" -> currentDrawableIconID = R.drawable.home
            "school" -> currentDrawableIconID = R.drawable.school
            "work" -> currentDrawableIconID = R.drawable.work
            "health" -> currentDrawableIconID = R.drawable.health
            "waning" -> currentDrawableIconID = R.drawable.warning
        }

        setSelectedIcon(imageViewColor, currentDrawableIconID)
    }
}