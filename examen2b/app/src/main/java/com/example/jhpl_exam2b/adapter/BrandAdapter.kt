package com.example.jhpl_exam2b.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.model.Brand

class BrandAdapter(
    private var brands: List<Brand>
) : RecyclerView.Adapter<BrandAdapter.ViewHolder>(){
    /* Methods */
    /* ---------------------------------------------- */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_brand, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandAdapter.ViewHolder, position: Int) {
        val brand = brands[position]
        holder.brandNameTextView.text = brand.name
    }

    override fun getItemCount(): Int {
        return brands.size
    }

    fun updateData(newBrands: List<Brand>) {
        brands = newBrands
        notifyDataSetChanged()
    }

    /* Classes */
    /* ---------------------------------------------- */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandNameTextView: TextView = itemView.findViewById(R.id.brandNameTextView)
    }

}