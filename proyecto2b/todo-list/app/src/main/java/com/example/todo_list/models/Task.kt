package com.example.todo_list.models

import java.util.*

data class Task(
    var taskCode: String? = null,
    var listCode: String? = null,
    var title: String? = null,
    var notes: String? = null,
    var status: String? = null,
    var deadline: Date? = null,
)