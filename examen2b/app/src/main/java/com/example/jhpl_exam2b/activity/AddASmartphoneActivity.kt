package com.example.jhpl_exam2b.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.core.app.NavUtils
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.adapter.SmartphoneAdapter
import com.example.jhpl_exam2b.firestore.FirestoreHelper
import com.example.jhpl_exam2b.model.Smartphone

class AddASmartphoneActivity : AppCompatActivity() {
    /* Attributes */
    /* ---------------------------------------------- */
    private val firestoreHelper = FirestoreHelper()
    private var brandId: String? = null
    private lateinit var smartphoneAdapter: SmartphoneAdapter

    /* Methods */
    /* ---------------------------------------------- */
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_asmartphone)

        // Local attributes
        val modelNameEditText: EditText = findViewById(R.id.editTxt_create_smartphone_model_name)
        val priceEditText: EditText = findViewById(R.id.editTxt_create_smartphone_price)
        val spinnerSmartphoneSerialType: Spinner = findViewById(R.id.spinner_create_smartphone_serial_type)
        val buttonCreateSmartphone: Button = findViewById(R.id.button_create_smartphone)

        // Set the spinner options
        setSpinnerOptions(spinnerSmartphoneSerialType)

        // Load data from Firestore and set it to the adapter
        brandId = intent.getStringExtra("brandId")

        buttonCreateSmartphone.setOnClickListener {
            val modelName = modelNameEditText.text.toString()
            val price = priceEditText.text.toString().toDouble()
            val smartphoneSerialType = spinnerSmartphoneSerialType.selectedItem.toString()

            val newSmartphone = Smartphone(
                brandId = brandId,
                modelName = modelName,
                price = price,
                serialType = smartphoneSerialType
            )

            firestoreHelper.addSmartphone(newSmartphone) { smartphoneId ->
                if (smartphoneId != null) {
                    smartphoneAdapter = SmartphoneAdapter(emptyList())
                    newSmartphone.id = smartphoneId
                    firestoreHelper.getSmartphonesByBrandId(brandId!!) {smartphones ->
                        smartphoneAdapter.updateData(smartphones)
                        smartphoneAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            this,
                            "Smartphone created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val intent = Intent(this@AddASmartphoneActivity, SmartphoneListActivity::class.java)
                    intent.putExtra("brandId", brandId)
                    startActivity(intent)
                    Log.d("MainActivity", "Added smartphone: $newSmartphone")
                } else {
                    Toast.makeText(
                        this,
                        "Error creating the smartphone",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MainActivity", "Error adding smartphone")
                }
            }
        }
    }

    private fun setSpinnerOptions(spinner: Spinner) {
        val options = resources.getStringArray(R.array.smartphone_serial_type_options).toMutableList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, SmartphoneListActivity::class.java)
                intent.putExtra("brandId", brandId)
                NavUtils.navigateUpTo(this, intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}