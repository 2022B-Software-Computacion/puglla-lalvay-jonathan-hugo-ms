package com.example.todo_list.activity

import android.content.ContentValues
import android.util.Log
import com.example.todo_list.models.Task
import com.example.todo_list.models.TaskList
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreHelper {
    /* Attributes */
    /* ---------------------------------------------- */
    private val db = Firebase.firestore

    /* Methods */
    /* ---------------------------------------------- */

    /* List CRUD operations */
    fun createList(list: TaskList, onComplete: (String?) -> Unit) {
        db.collection("lists")
            .add(list)
            .addOnSuccessListener { documentReference ->
                val listCode = documentReference.id
                db.collection("lists").document(listCode)
                    .update("listCode", listCode)
                    .addOnSuccessListener {
                        onComplete(documentReference.id)
                    }
                    .addOnFailureListener {
                        onComplete(null)
                    }
            }
    }

    fun updateList(listCode: String, updatedList: TaskList, onComplete: (Boolean) -> Unit) {
        db.collection("lists")
            .document(listCode)
            .set(updatedList)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error updating list: ", exception)
                onComplete(false)
            }
    }

    fun getListByCode(listCode: String, onComplete: (TaskList?) -> Unit) {
        db.collection("lists")
            .document(listCode)
            .get()
            .addOnSuccessListener { document ->
                val list = document.toObject(TaskList::class.java)
                list?.listCode = document.id
                onComplete(list)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting list by its code: ${exception.message}")
                onComplete(null)
            }
    }

    fun getListByName(listName: String, onComplete: (TaskList?) -> Unit) {
        db.collection("lists")
            .whereEqualTo("name", listName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val taskList = document.toObject(TaskList::class.java)
                    taskList?.listCode = document.id
                    onComplete(taskList)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting task lists by name: ${exception.message}")
                onComplete(null)
            }
    }


    fun getAllLists(onComplete: (List<TaskList>?) -> Unit) {
        db.collection("lists")
            .get()
            .addOnSuccessListener { result ->
                val lists = mutableListOf<TaskList>()
                for (document in result) {
                    val list = document.toObject(TaskList::class.java)
                    list.listCode = document.id
                    lists.add(list)
                }
                onComplete(lists)
            }
            .addOnFailureListener {exception ->
                Log.d(ContentValues.TAG, "Error getting lists: ${exception.message}")
                onComplete(emptyList())
            }
    }

    fun deleteList(listCode: String, onComplete: (Boolean) -> Unit) {
        db.collection("lists")
            .document(listCode)
            .delete()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    /* Task CRUD operations */
    fun createTask(task: Task, onComplete: (String?) -> Unit) {
        db.collection("lists").document(task.listCode!!)
            .collection("tasks")
            .add(task)
            .addOnSuccessListener { documentReference ->
                val taskCode = documentReference.id
                db.collection("lists").document(task.listCode!!)
                    .collection("tasks").document(taskCode)
                    .update("taskCode", taskCode)
                    .addOnSuccessListener {
                        onComplete(documentReference.id)
                    }
                    .addOnFailureListener {
                        onComplete(null)
                    }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    fun getTasksByListCode(listCode: String, onComplete: (List<Task>) -> Unit) {
        db.collection("lists")
            .document(listCode)
            .collection("tasks")
            .get()
            .addOnSuccessListener { documents ->
                val tasks = mutableListOf<Task>()
                for (document in documents) {
                    val task = document.toObject(Task::class.java)
                    tasks.add(task)

                }
                onComplete(tasks)
            }
            .addOnFailureListener {exception ->
                Log.d(ContentValues.TAG, "Error getting tasks: ${exception.message}")
                onComplete(emptyList())
            }
    }

    fun getTaskById(listCode: String, taskCode: String, onComplete: (Task?) -> Unit) {
        db.collection("lists")
            .document(listCode)
            .collection("tasks")
            .document(taskCode)
            .get()
            .addOnSuccessListener { document ->
                val task = document.toObject(Task::class.java)
                onComplete(task)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting task: ${exception.message}")
                onComplete(null)
            }
    }

    fun updateTask(task: Task, onComplete: (Boolean) -> Unit) {
        db.collection("lists")
            .document(task.listCode!!)
            .collection("tasks")
            .document(task.taskCode!!)
            .set(task)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error updating task: ${exception.message}")
                onComplete(false)
            }
    }

    fun deleteTask(taskCode: String, listCode: String, onComplete: (Boolean) -> Unit) {
        db.collection("lists")
            .document(listCode)
            .collection("tasks")
            .document(taskCode)
            .delete()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error deleting task: ${exception.message}")
                onComplete(false)
            }
    }

    fun getAllTasks(onComplete: (List<Task>) -> Unit) {
        db.collection("lists")
            .get()
            .addOnSuccessListener { lists ->
                val tasks = mutableListOf<Task>()
                for (list in lists) {
                    db.collection("lists").document(list.id).collection("tasks")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val task = document.toObject(Task::class.java)
                                tasks.add(task)
                            }
                            onComplete(tasks)
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "Error getting tasks: ${exception.message}")
                            onComplete(emptyList())
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting lists: ${exception.message}")
                onComplete(emptyList())
            }
    }



}