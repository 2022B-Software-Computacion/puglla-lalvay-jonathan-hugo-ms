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
import com.example.jhpl_exam2b.model.Brand
import com.google.firebase.firestore.ListenerRegistration

class BrandAdapter(
    private var brands: List<Brand>
) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>(){
    /* Attributes */
    /* ---------------------------------------------- */
    private lateinit var context: Context
    private lateinit var firestoreHelper: FirestoreHelper
    private lateinit var listenerRegistration: ListenerRegistration
    private lateinit var brandAdapter: BrandAdapter
    private var onBrandClickListener: OnBrandClickListener? = null

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
    ): BrandAdapter.BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_brand, parent, false
        )
        brandAdapter = BrandAdapter(brands)
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

    fun setOnBrandClickListener(listener: OnBrandClickListener) {
        onBrandClickListener = listener
    }

    interface OnBrandClickListener {
        fun onBrandClick(brandId: String)
        fun onOptionsItemSelected(item: MenuItem): Boolean
    }

    /* View Holder Class */
    /* ---------------------------------------------- */
    inner class BrandViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* Attributes */
        /* ---------------------------------------------- */
        private val brandNameTextView: TextView = itemView.findViewById(R.id.brandNameTextView)
        private val brandOptionsButton: ImageButton = itemView.findViewById(R.id.brand_options)

        /* Methods */
        /* ---------------------------------------------- */
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
                        onBrandClickListener?.onBrandClick(brand.id!!)
                        onBrandClickListener?.onOptionsItemSelected(menuItem)
                        true
                    }
                    R.id.delete_brand_menu_item -> {
                        onBrandClickListener?.onBrandClick(brand.id!!)
                        onBrandClickListener?.onOptionsItemSelected(menuItem)
                        true
                    }
                    R.id.view_smartphones_menu_item -> {
                        onBrandClickListener?.onBrandClick(brand.id!!)
                        onBrandClickListener?.onOptionsItemSelected(menuItem)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}