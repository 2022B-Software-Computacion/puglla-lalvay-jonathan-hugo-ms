package com.example.jhpl_exam2b.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.core.app.NavUtils
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.adapter.SmartphoneAdapter
import com.example.jhpl_exam2b.firestore.FirestoreHelper
import com.example.jhpl_exam2b.model.Smartphone

class EditSmartphoneActivity : AppCompatActivity() {
    /* Attributes */
    /* ---------------------------------------------- */
    private var brandId: String? = null
    private var smartphoneId: String? = null
    private val firestoreHelper = FirestoreHelper()
    private lateinit var smartphoneAdapter: SmartphoneAdapter


    /* Methods */
    /* ---------------------------------------------- */
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_smartphone)

        // Local attributes
        val modelNameEditText: EditText = findViewById(R.id.editTxt_smartphone_model_name)
        val priceEditText: EditText = findViewById(R.id.editTxt_smartphone_price)
        val serialTypeSpinner: Spinner = findViewById(R.id.spinner_smartphone_serial_type)
        val saveSmartphoneChangesButton: Button = findViewById(R.id.button_edit_smartphone)

        // Set the spinner options
        initSpinnerOptions(serialTypeSpinner)

        // Load data from Firestore and set it to the adapter
        brandId = intent.getStringExtra("brandId")
        smartphoneId = intent.getStringExtra("smartphoneId")

        if(brandId != null && smartphoneId != null) {
            setSmartphoneModelNameToEditText(brandId!!, smartphoneId!!, modelNameEditText)
            setSmartphonePriceToEditText(brandId!!, smartphoneId!!, priceEditText)
            setSmartphoneSerialTypeToSpinner(brandId!!, smartphoneId!!, serialTypeSpinner)
        }

        saveSmartphoneChangesButton.setOnClickListener {
            val smartphoneModelName = modelNameEditText.text.toString()
            val smartphonePrice = priceEditText.text.toString().toDouble()
            val smartphoneSerialType = serialTypeSpinner.selectedItem.toString()

            val updatedSmartphone = Smartphone(
                id = smartphoneId!!,
                brandId = brandId!!,
                modelName = smartphoneModelName,
                price = smartphonePrice,
                serialType = smartphoneSerialType
            )

            firestoreHelper.updateSmartphone(updatedSmartphone) { isSuccess ->
                if (isSuccess) {
                    smartphoneAdapter = SmartphoneAdapter(emptyList())
                    firestoreHelper.getSmartphonesByBrandId(brandId!!) {smartphones ->
                        smartphoneAdapter.updateData(smartphones)
                        smartphoneAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            this,
                            "Smartphone updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, SmartphoneListActivity::class.java)
                        intent.putExtra("brandId", brandId)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Error updating smartphone",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initSpinnerOptions(spinner: Spinner) {
        val options = resources.getStringArray(R.array.smartphone_serial_type_options).toMutableList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setSmartphoneSerialTypeToSpinner(
        brandId: String, smartphoneId: String, spinner: Spinner
    ) {
        firestoreHelper.getSmartphoneById(brandId, smartphoneId) { smartphone ->
            smartphone?.let {
                val smartphoneSerialTypeOptions = resources.getStringArray(
                    R.array.smartphone_serial_type_options
                )
                val defaultStatusIndex = smartphoneSerialTypeOptions.indexOf(it.serialType)
                spinner.setSelection(defaultStatusIndex)
            }
        }
    }

    private fun setSmartphoneModelNameToEditText
                (brandId: String, smartphoneId: String, editText: EditText
    ) {
        firestoreHelper.getSmartphoneById(brandId, smartphoneId) { smartphone ->
            smartphone?.let {
                editText.setText(it.modelName)
            }
        }
    }

    private fun setSmartphonePriceToEditText(
        brandId: String, smartphoneId: String, editText: EditText
    ) {
        firestoreHelper.getSmartphoneById(brandId, smartphoneId) { smartphone ->
            smartphone?.let {
                editText.setText(it.price.toString())
            }
        }
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