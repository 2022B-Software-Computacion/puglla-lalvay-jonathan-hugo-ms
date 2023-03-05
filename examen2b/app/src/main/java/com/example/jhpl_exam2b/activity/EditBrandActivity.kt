package com.example.jhpl_exam2b.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.firestore.FirestoreHelper

class EditBrandActivity : AppCompatActivity() {
    /* Attributes */
    /* ---------------------------------------------- */
    private var brandId: String? = null
    private val firestoreHelper = FirestoreHelper()

    /* Methods */
    /* ---------------------------------------------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_brand)

        // Local attributes
        val nameEditText: EditText = findViewById(R.id.editTxt_brand_name)
        val priceEditText: EditText = findViewById(R.id.editTxt_brand_price)
        val spinner: Spinner = findViewById(R.id.spinner_brand_status)
        val checkBox: CheckBox = findViewById(R.id.checkbox_brand_has_a_webpage)

        // Set the spinner options
        initSpinnerOptions(spinner)

        // Load data from Firestore and set it to the adapter
        brandId = intent.getStringExtra("brandId")
        if (brandId != null) {
            setBrandNameToEditText(brandId!!, nameEditText)
            setBrandPriceToEditText(brandId!!, priceEditText)
            setBrandStatusToSpinner(brandId!!, spinner)
            setBrandHasAWebPageToCheckBox(brandId!!, checkBox)
        }
    }

    private fun setBrandHasAWebPageToCheckBox(brandId: String, checkBox: CheckBox) {
        firestoreHelper.getBrandById(brandId) { brand ->
            brand?.let {
                checkBox.isChecked = it.hasAWebPage!!
            }
        }
    }

    private fun initSpinnerOptions(spinner: Spinner) {
        // A -> Active, N -> New, B -> Banned
        val options = arrayOf("Active", "New", "Banned")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setBrandStatusToSpinner(brandId: String, spinner: Spinner) {
        firestoreHelper.getBrandById(brandId) { brand ->
            brand?.let {
                val statusOptions = resources.getStringArray(R.array.brand_status_options)
                val defaultStatusIndex = statusOptions.indexOf(it.status)
                spinner.setSelection(defaultStatusIndex)
            }
        }
    }

    private fun setBrandNameToEditText(brandId: String, editText: EditText) {
        firestoreHelper.getBrandById(brandId) { brand ->
            brand?.let {
                editText.hint = "${it.name}"
            }
        }
    }

    private fun setBrandPriceToEditText(brandId: String, editText: EditText) {
        firestoreHelper.getBrandById(brandId) { brand ->
            brand?.let {
                editText.hint = "$${it.price}"
            }
        }
    }
}