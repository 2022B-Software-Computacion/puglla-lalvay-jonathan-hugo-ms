package com.example.todo_list.models

data class TaskList(
    var listCode: String? = null,
    var name: String? = null,
    var description: String? = null,
    var hexColor: String? = null,
    var iconPath: String? = null
)