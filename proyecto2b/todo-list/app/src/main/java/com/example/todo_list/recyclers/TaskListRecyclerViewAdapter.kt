package com.example.todo_list.recyclers

import android.annotation.SuppressLint
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.activity.FirestoreHelper
import com.example.todo_list.activity.ReminderActivity
import com.example.todo_list.models.TaskList

class TaskListRecyclerViewAdapter(
    private val parentContext: ReminderActivity,
    private var list: ArrayList<TaskList>
): RecyclerView.Adapter<TaskListRecyclerViewAdapter.MyViewHolder>() {

    private lateinit var taskRecyclerViewAdapter: TaskListRecyclerViewAdapter

    inner class MyViewHolder(view: View):
        RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        val nameTextView: TextView
        val iconImageView: ImageView
        val numberTextView: TextView

        init {
            nameTextView = view.findViewById(R.id.tv_list_name)
            iconImageView = view.findViewById(R.id.img_list)
            numberTextView = view.findViewById(R.id.tv_list_tasks_number)

            view.setOnCreateContextMenuListener(this)

            // Setting the view selection mode
            itemView.isClickable = true
            itemView.isLongClickable = true

            itemView.setOnClickListener {
                parentContext.goToTaskActivityWithParameters(
                    list[adapterPosition].name!!,
                    list[adapterPosition].listCode!!
                )
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            if (menu != null) {
                val inflater = MenuInflater(view?.context)
                inflater.inflate(R.menu.todo_menu, menu)

                parentContext.setSelectedTaskListCode(list[adapterPosition].listCode)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_list_adapter,
                parent,
                false
            )

        taskRecyclerViewAdapter = TaskListRecyclerViewAdapter(parentContext, list)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentList = this.list[position]
        var iconBackgroundColor = R.drawable.list_image
        var iconImage = R.drawable.default_icon

        // Setting a drawable color by hex color
        when (currentList.hexColor) {
            "#FF2E63" -> {
                iconBackgroundColor = R.drawable.color_pink
            }
            "#08D9D6" -> {
                iconBackgroundColor = R.drawable.color_cyan
            }
            "#F38181" -> {
                iconBackgroundColor = R.drawable.color_orange
            }
            "#6A2C70" -> {
                iconBackgroundColor = R.drawable.color_purple
            }
            "#FFDE7D" -> {
                iconBackgroundColor = R.drawable.color_yellow
            }
        }

        // Setting an icon by icon path
        when (currentList.iconPath!!.lowercase()) {
            "home" -> {
                iconImage = R.drawable.home
            }
            "school" -> {
                iconImage = R.drawable.school
            }
            "work" -> {
                iconImage = R.drawable.work
            }
            "health" -> {
                iconImage = R.drawable.health
            }
            "warning" -> {
                iconImage = R.drawable.warning
            }
        }

        holder.nameTextView.text = currentList.name
        holder.iconImageView.setBackgroundResource(iconBackgroundColor)
        holder.iconImageView.setImageResource(iconImage)
        val helper = FirestoreHelper()
        helper.getTasksByListCode(currentList.listCode!!) { taskList ->
            holder.numberTextView.text = taskList.size.toString() + " >"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newLists: List<TaskList>) {
        list = ArrayList(newLists.toMutableList())
        notifyDataSetChanged()
    }
}