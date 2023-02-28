package com.example.jhpl_exam2b.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.firestore.FireStoreHelper
import com.example.jhpl_exam2b.model.Brand
import com.google.firebase.firestore.ListenerRegistration

class BrandAdapter(
    private var brands: List<Brand>
) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>(){
    /* Attributes */
    /* ---------------------------------------------- */
    private lateinit var context: Context
    private lateinit var fireStoreHelper: FireStoreHelper
    private lateinit var listenerRegistration: ListenerRegistration // declare the variable here
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        fireStoreHelper = FireStoreHelper()
    }

    /* Methods */
    /* ---------------------------------------------- */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandAdapter.BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_brand, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandAdapter.BrandViewHolder, position: Int) {
        val brand = brands[position]
        holder.bind(brand)
    }

    override fun getItemCount(): Int {
        return brands.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newBrands: List<Brand>) {
        brands = newBrands
        notifyDataSetChanged()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        listenerRegistration.remove()
    }

    inner class BrandViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* Attributes */
        /* ---------------------------------------------- */
        private val brandNameTextView: TextView = itemView.findViewById(R.id.brandNameTextView)
        private val brandOptionsButton: ImageButton = itemView.findViewById(R.id.brand_options)

        fun bind(brand: Brand) {
            brandNameTextView.text = brand.name

            brandOptionsButton.setOnClickListener {
                showBrandOptionsMenu(brand)
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun showBrandOptionsMenu(brand: Brand) {
            val popupMenu = PopupMenu(itemView.context, brandOptionsButton)
            popupMenu.menuInflater.inflate(R.menu.brand_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit_brand_menu_item -> {
                        // Handle edit brand action
                        true
                    }
                    R.id.delete_brand_menu_item -> {
                        AlertDialog.Builder(context)
                            .setMessage("Are you sure you want to delete this brand?")
                            .setPositiveButton("Delete") { dialog, _ ->
                                // Delete the brand and associated smartphones from FireStore
                                // Delete the brand from FireStore
                                fireStoreHelper.deleteBrand(brand.id!!) { isSuccess ->
                                    if (isSuccess) {
                                        // Fetch updated data from FireStore
                                        fireStoreHelper.getBrands {
                                            // Pass updated data to the adapter
                                            // Notify the adapter of the data change
                                            notifyDataSetChanged()
                                            Toast.makeText(context, "Brand deleted successfully", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "Error deleting brand", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                        true
                    }
                    R.id.view_smartphones_menu_item -> {
                        // Handle view smartphones action
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}