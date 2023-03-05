package com.example.jhpl_exam2b.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.adapter.SmartphoneAdapter
import com.example.jhpl_exam2b.firestore.FirestoreHelper

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SmartphoneListFragment : Fragment(), SmartphoneAdapter.OnSmartphoneClickListener {
    /* Attributes */
    /* ---------------------------------------------- */
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var smartphoneAdapter: SmartphoneAdapter
    private val firestoreHelper = FirestoreHelper()
    private var selectedSmartphoneId: String? = null
    private var brandId: String? = null

    /* Methods */
    /* ---------------------------------------------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSmartphoneClick(smartphoneId: String) {
        // Update the selected Smartphone ID
        selectedSmartphoneId = smartphoneId
        // Refresh the adapter to highlight the selected smartphone
        smartphoneAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_smartphone_list, container, false)
        recyclerView = view.findViewById(R.id.smartphoneRV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize adapter and set it to RecyclerView
        smartphoneAdapter = SmartphoneAdapter(emptyList())
        smartphoneAdapter.setOnSmartphoneClickListener(this)
        recyclerView.adapter = smartphoneAdapter

        // Load data from Firestore and set it to the adapter
        brandId = arguments?.getString("brandId")
        if (brandId != null) {
            firestoreHelper.getSmartphonesByBrandId(brandId!!) { smartphones ->
                smartphoneAdapter.updateData(smartphones)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.edit_smartphone_menu_item -> {
                true
            }
            R.id.delete_smartphone_menu_item -> {
                AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this smartphone?")
                    .setPositiveButton("Delete") {dialog, _ ->
                        // Delete the smartphone from FireStore
                        firestoreHelper.deleteSmartphone(selectedSmartphoneId!!, brandId!!){ isSuccess ->
                            if (isSuccess) {
                                firestoreHelper.getSmartphonesByBrandId(selectedSmartphoneId!!) {
                                    smartphones ->
                                    // Pass updated data to the adapter
                                    smartphoneAdapter.updateData(smartphones)
                                    // Notify the adapter of the data change
                                    Toast.makeText(
                                        context,
                                        "Smartphone deleted successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error deleting smartphone",
                                    Toast.LENGTH_SHORT
                                ).show()
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
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}