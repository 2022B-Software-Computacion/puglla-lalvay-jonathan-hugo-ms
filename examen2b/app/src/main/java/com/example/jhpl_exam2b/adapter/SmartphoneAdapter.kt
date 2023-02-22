package com.example.jhpl_exam2b.adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.carrier.CarrierMessagingService.SendMmsResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.model.Smartphone

class SmartphoneAdapter(
    private val smartphones: List<Smartphone>
    ) : RecyclerView.Adapter<SmartphoneAdapter.ViewHolder>() {

    /* Methods */
    /* ---------------------------------------------- */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SmartphoneAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_smartphone, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmartphoneAdapter.ViewHolder, position: Int) {
        val smartphone = smartphones[position]
        holder.smartphoneNameTextView.text = smartphone.modelName
        // holder.priceTextView.text = String.format("$%.2f", smartphone.price)
        // holder.brandNameTextView.text = smartphone.brandName
    }

    override fun getItemCount(): Int {
        return smartphones.size
    }

    /* Classes */
    /* ---------------------------------------------- */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val smartphoneNameTextView: TextView = itemView.findViewById(R.id.smartphoneNameTxt)
        // val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        // val brandNameTextView: TextView = itemView.findViewById(R.id.brandNameTextView)
        init {
            itemView.setOnClickListener {
                // TODO: Handle click event
            }
        }
    }
}