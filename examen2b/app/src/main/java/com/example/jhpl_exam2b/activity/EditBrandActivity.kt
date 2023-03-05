package com.example.jhpl_exam2b.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.adapter.BrandAdapter
import com.example.jhpl_exam2b.firestore.FirestoreHelper
import com.example.jhpl_exam2b.model.Brand

class EditBrandActivity : AppCompatActivity() {
    /* Attributes */
    /* ---------------------------------------------- */
    private var brandId: String? = null
    private val firestoreHelper = FirestoreHelper()
    private lateinit var brandAdapter: BrandAdapter

    /* Methods */
    /* ---------------------------------------------- */
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_brand)

        // Local attributes
        val nameEditText: EditText = findViewById(R.id.editTxt_brand_name)
        val priceEditText: EditText = findViewById(R.id.editTxt_brand_price)
        val spinnerBrandStatus: Spinner = findViewById(R.id.spinner_brand_status)
        val checkBox: CheckBox = findViewById(R.id.checkbox_brand_has_a_webpage)
        val saveButton: Button = findViewById(R.id.button_edit_brand)

        // Set the spinner options
        initSpinnerOptions(spinnerBrandStatus)

        // Load data from Firestore and set it to the adapter
        brandId = intent.getStringExtra("brandId")
        if (brandId != null) {
            setBrandNameToEditText(brandId!!, nameEditText)
            setBrandPriceToEditText(brandId!!, priceEditText)
            setBrandStatusToSpinner(brandId!!, spinnerBrandStatus)
            setBrandHasAWebPageToCheckBox(brandId!!, checkBox)
        }

        saveButton.setOnClickListener {
            val brandName = nameEditText.text.toString()
            val brandPrice = priceEditText.text.toString().toDouble()
            val brandStatus = spinnerBrandStatus.selectedItem.toString()
            val brandHasAWebPage = checkBox.isChecked

            val updatedBrand = Brand(
                id = brandId!!,
                name = brandName, 
                price = brandPrice, 
                status = brandStatus, 
                hasAWebPage = brandHasAWebPage
            )
            
            firestoreHelper.updateBrand(brandId!!, updatedBrand) {isSuccess ->
                if (isSuccess) {
                    brandAdapter = BrandAdapter(emptyList())
                    firestoreHelper.getAllBrands { brands ->
                        brandAdapter.updateData(brands!!)
                        brandAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            this,
                            "Brand updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Error updating brand",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initSpinnerOptions(spinner: Spinner) {
        // A -> Active, N -> New, B -> Banned
        val options = resources.getStringArray(R.array.brand_status_options).toMutableList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setBrandHasAWebPageToCheckBox(brandId: String, checkBox: CheckBox) {
        firestoreHelper.getBrandById(brandId) { brand ->
            brand?.let {
                checkBox.isChecked = it.hasAWebPage!!
            }
        }
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
                editText.setText(it.name)
            }
        }
    }

    private fun setBrandPriceToEditText(brandId: String, editText: EditText) {
        firestoreHelper.getBrandById(brandId) { brand ->
            brand?.let {
                editText.setText(it.price.toString())
            }
        }
    }
}