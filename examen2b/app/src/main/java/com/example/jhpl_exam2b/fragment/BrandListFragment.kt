package com.example.jhpl_exam2b.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.adapter.BrandAdapter
import com.example.jhpl_exam2b.model.Brand
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BrandListFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var brandAdapter: BrandAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_brand, container, false)

        recyclerView = view.findViewById(R.id.brandRV)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        brandAdapter = BrandAdapter(emptyList())
        recyclerView.adapter = brandAdapter

        getBrands()

        return view
    }

    private fun getBrands() {
        val db = FirebaseFirestore.getInstance()
        db.collection("brands")
            .get()
            .addOnSuccessListener { result ->
                val brands = mutableListOf<Brand>()
                for (document in result) {
                    val brand = document.toObject<Brand>()
                    brand.id = document.id
                    brands.add(brand)
                }
                brandAdapter.updateData(brands)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting brands", exception)
            }
    }

    companion object {
        private const val TAG = "BrandListFragment"
    }
}