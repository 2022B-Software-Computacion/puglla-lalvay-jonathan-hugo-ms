package com.example.jhpl_exam2b.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.adapter.BrandAdapter
import com.example.jhpl_exam2b.firestore.FirestoreHelper
import com.example.jhpl_exam2b.model.Brand

class AddABrandActivity : AppCompatActivity() {
    /* Attributes */
    /* ---------------------------------------------- */
    private val firestoreHelper = FirestoreHelper()
    private lateinit var brandAdapter: BrandAdapter

    /* Methods */
    /* ---------------------------------------------- */
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_abrand)

        // Local attributes
        val nameEditText: EditText = findViewById(R.id.editTxt_create_brand_name)
        val priceEditTest: EditText = findViewById(R.id.editTxt_add_a_brand_price)
        val spinnerBrandStatus: Spinner = findViewById(R.id.spinner_add_a_brand_status)
        val checkBoxHasAWebpage: CheckBox = findViewById(R.id.checkbox_add_brand_has_a_webpage)
        val saveNewBrandButton: Button = findViewById(R.id.button_save_new_brand)

        // Set the spinner options
        setSpinnerOptions(spinnerBrandStatus)

        saveNewBrandButton.setOnClickListener {
            val brandName = nameEditText.text.toString()
            val brandPrice = priceEditTest.text.toString().toDouble()
            val brandStatus = spinnerBrandStatus.selectedItem.toString()
            val brandHasAWepPage = checkBoxHasAWebpage.isChecked

            val newBrand = Brand(
                name = brandName,
                price = brandPrice,
                status = brandStatus,
                hasAWebPage = brandHasAWepPage
            )

            firestoreHelper.addBrand(newBrand) { brandId ->
                if (brandId != null) {
                    newBrand.id = brandId
                    brandAdapter = BrandAdapter(emptyList())
                    firestoreHelper.getAllBrands { brands ->
                        brandAdapter.updateData(brands!!)
                        brandAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            this,
                            "Brand created successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    Log.d("MainActivity", "Created brand: $newBrand")
                } else {
                    Toast.makeText(
                        this,
                        "Error creating the brand",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MainActivity", "Error creating the brand")
                }
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSpinnerOptions(spinner: Spinner) {
        val options = resources.getStringArray(R.array.brand_status_options).toMutableList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}