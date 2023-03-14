package com.example.todo_list.recyclers

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.activity.TaskActivity
import com.example.todo_list.models.Task
import com.google.android.material.textfield.TextInputEditText

class TaskRecyclerViewAdapter(
    private val parentContext: TaskActivity,
    private var list: ArrayList<Task>
): RecyclerView.Adapter<TaskRecyclerViewAdapter.MyViewHolder>() {

    private lateinit var taskRecyclerViewAdapter: TaskRecyclerViewAdapter

    inner class MyViewHolder(view: View):
        RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        val titleText: TextInputEditText
        val notesText: TextInputEditText
        val datelineText: TextInputEditText
        val statusText: TextInputEditText

        init {
            titleText = view.findViewById(R.id.input_note_title)
            notesText = view.findViewById(R.id.input_note_content)
            datelineText = view.findViewById(R.id.input_date_time)
            statusText = view.findViewById(R.id.input_status)

            view.setOnCreateContextMenuListener(this)

            // Setting the view selection mode
            itemView.isClickable = true
            itemView.isLongClickable = true
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            if (menu != null) {
                val inflater = MenuInflater(view?.context)
                inflater.inflate(R.menu.task_menu, menu)

                parentContext.setSelectedTaskCode(list[adapterPosition].taskCode)
                parentContext.setSelectedListCode(list[adapterPosition].listCode)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_task_adapter,
                parent,
                false
            )

        taskRecyclerViewAdapter = TaskRecyclerViewAdapter(parentContext, list)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTask = this.list[position]

        holder.titleText.setText(currentTask.title)
        holder.notesText.setText(currentTask.notes)
        holder.statusText.setText(currentTask.status)

        if(currentTask.deadline != null) {
            holder.datelineText.setText(currentTask.deadline.toString())
        }
        else {
            holder.datelineText.setText("No Date")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newLists: List<Task>) {
        list = ArrayList(newLists.toMutableList())
        notifyDataSetChanged()
    }

}