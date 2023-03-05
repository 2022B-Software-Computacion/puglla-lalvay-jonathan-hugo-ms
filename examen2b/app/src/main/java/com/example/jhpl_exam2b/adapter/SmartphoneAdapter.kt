package com.example.jhpl_exam2b.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.firestore.FirestoreHelper
import com.example.jhpl_exam2b.model.Smartphone
import com.google.firebase.firestore.ListenerRegistration

class SmartphoneAdapter(
    private var smartphones: List<Smartphone>
    ) : RecyclerView.Adapter<SmartphoneAdapter.ViewHolder>() {
    /* Attributes */
    /* ---------------------------------------------- */
    private lateinit var context: Context
    private lateinit var firestoreHelper: FirestoreHelper
    private lateinit var listenerRegistration: ListenerRegistration
    private lateinit var smartphoneAdapter: SmartphoneAdapter
    private var onSmartphoneClickListener: OnSmartphoneClickListener? = null

    /* Methods */
    /* ---------------------------------------------- */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        firestoreHelper = FirestoreHelper()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SmartphoneAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_smartphone, parent, false)
        smartphoneAdapter = SmartphoneAdapter(smartphones)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmartphoneAdapter.ViewHolder, position: Int) {
        val smartphone = smartphones[position]
        holder.bind(smartphone)
    }

    override fun getItemCount(): Int {
        return smartphones.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newSmartphones: List<Smartphone>) {
        smartphones = newSmartphones
        notifyDataSetChanged()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        listenerRegistration.remove()
    }

    fun setOnSmartphoneClickListener(listener: OnSmartphoneClickListener) {
        onSmartphoneClickListener = listener
    }

    interface OnSmartphoneClickListener {
        fun onSmartphoneClick(smartphoneId: String)

        fun onOptionsItemSelected(item: MenuItem): Boolean
    }

    /* Classes */
    /* ---------------------------------------------- */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val smartphoneNameTextView: TextView = itemView.findViewById(R.id.smartphoneNameTextView)
        private val smartphoneOptionsButton: ImageButton = itemView.findViewById(R.id.smartphone_options)

        fun bind(smartphone: Smartphone) {
            smartphoneNameTextView.text = smartphone.modelName
            smartphoneOptionsButton.setOnClickListener{
                showSmartphoneOptionsMenu(smartphone)
            }
        }

        private fun showSmartphoneOptionsMenu(smartphone: Smartphone) {
            val popupMenu = PopupMenu(itemView.context, smartphoneOptionsButton)
            popupMenu.menuInflater.inflate(R.menu.smartphone_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.edit_smartphone_menu_item -> {
                        true
                    }
                    R.id.delete_smartphone_menu_item -> {
                        onSmartphoneClickListener?.onSmartphoneClick(smartphone.id!!)
                        onSmartphoneClickListener?.onOptionsItemSelected(menuItem)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popupMenu.show()
        }

    }
}