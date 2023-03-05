package com.example.jhpl_exam2b.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.firestore.FirestoreHelper
import com.example.jhpl_exam2b.fragment.SmartphoneListFragment

class SmartphoneListActivity : AppCompatActivity() {
    /* Attributes */
    /* ---------------------------------------------- */
    private val firestoreHelper = FirestoreHelper()

    /* Methods */
    /* ---------------------------------------------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smartphone_list)
        val titleTextView: TextView = findViewById(R.id.smartphoneNameTitleTxt)

        val brandId = intent.getStringExtra("brandId")
        val bundle = Bundle()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val smartphoneListFragment = SmartphoneListFragment()

        setBrandNameToTextView(brandId!!, titleTextView)
        bundle.putString("brandId", brandId)
        smartphoneListFragment.arguments = bundle
        fragmentTransaction.add(R.id.fragment_smartphone, smartphoneListFragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("SetTextI18n")
    private fun setBrandNameToTextView(brandId: String, textView: TextView) {
        firestoreHelper.getBrandById(brandId) { brand ->
            brand?.let {
                textView.text = "${it.name} Smartphones"
            }
        }
    }
}