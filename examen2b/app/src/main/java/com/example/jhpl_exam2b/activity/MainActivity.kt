package com.example.jhpl_exam2b.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.adapter.BrandAdapter
import com.example.jhpl_exam2b.firestore.FirestoreHelper
import com.example.jhpl_exam2b.fragment.BrandListFragment
import com.example.jhpl_exam2b.model.Brand
import com.example.jhpl_exam2b.model.Smartphone

class MainActivity : AppCompatActivity() {
    /* Attributes */
    /* ---------------------------------------------- */
    private val firestoreHelper = FirestoreHelper()
    private lateinit var adapter: BrandAdapter
    private lateinit var recyclerView: RecyclerView
    private var brands: ArrayList<Brand> = ArrayList()
    /* Methods */
    /* ---------------------------------------------- */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val brandListFragment = BrandListFragment()
        fragmentTransaction.add(R.id.fragment_brand, brandListFragment)
        fragmentTransaction.commit()

        val newBrand1 = Brand(name = "Test Brand 1", price = 100.0, status = "A", hasAWebPage = true)
        val newBrand2 = Brand(name = "Test Brand 2", price = 200.0, status = "A", hasAWebPage = true)
        val newBrand3 = Brand(name = "Test Brand 3", price = 300.0, status = "A", hasAWebPage = true)
        brands.add(newBrand1)
        brands.add(newBrand2)
        brands.add(newBrand3)
        //recyclerView = findViewById(R.id.brandRV)
//        val layoutManager = GridLayoutManager(this, 2)
//        recyclerView.layoutManager = layoutManager
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = BrandAdapter(brands)
//        recyclerView.adapter = adapter


        firestoreHelper.addBrand(newBrand1) { brandId ->
            if (brandId != null) {
                newBrand1.id = brandId
                Log.d("MainActivity", "Added brand: $newBrand1")

                // Create a new smartphone for the brand
                val newSmartphone = Smartphone(
                    modelName = "Test Smartphone 1",
                    price = 1000.0,
                    brandId = newBrand1.id,
                    serialType = "M",
                )
                firestoreHelper.addSmartphone(newSmartphone) { smartphoneId ->
                    if (smartphoneId != null) {
                        newSmartphone.id = smartphoneId
                        Log.d("MainActivity", "Added smartphone: $newSmartphone")
                    } else {
                        Log.e("MainActivity", "Error adding smartphone")
                    }
                }
            } else {
                Log.e("MainActivity", "Error adding brand")
            }
        }


        firestoreHelper.addBrand(newBrand2) { brandId ->
            if (brandId != null) {
                newBrand2.id = brandId
                Log.d("MainActivity", "Added brand: $newBrand2")

                // Create a new smartphone for the brand
                val newSmartphone = Smartphone(
                    modelName = "Test Smartphone 2",
                    price = 1000.0,
                    brandId = newBrand2.id,
                    serialType = "M",
                )
                firestoreHelper.addSmartphone(newSmartphone) { smartphoneId ->
                    if (smartphoneId != null) {
                        newSmartphone.id = smartphoneId
                        Log.d("MainActivity", "Added smartphone: $newSmartphone")
                    } else {
                        Log.e("MainActivity", "Error adding smartphone")
                    }
                }
            } else {
                Log.e("MainActivity", "Error adding brand")
            }
        }


        firestoreHelper.addBrand(newBrand3) { brandId ->
            if (brandId != null) {
                newBrand3.id = brandId
                Log.d("MainActivity", "Added brand: $newBrand3")

                // Create a new smartphone for the brand
                val newSmartphone = Smartphone(
                    modelName = "Test Smartphone 3",
                    price = 1000.0,
                    brandId = newBrand3.id,
                    serialType = "M",
                )
                firestoreHelper.addSmartphone(newSmartphone) { smartphoneId ->
                    if (smartphoneId != null) {
                        newSmartphone.id = smartphoneId
                        Log.d("MainActivity", "Added smartphone: $newSmartphone")
                    } else {
                        Log.e("MainActivity", "Error adding smartphone")
                    }
                }
            } else {
                Log.e("MainActivity", "Error adding brand")
            }
        }
//        firestoreHelper.addBrand(newBrand) { brandId ->
//            if (brandId != null) {
//                newBrand.id = brandId
//                Log.d("MainActivity", "Added brand: $newBrand")
//
//                // Create a new smartphone for the brand
//                val newSmartphone = Smartphone(
//                    modelName = "Test Smartphone",
//                    price = 1000.0,
//                    brandId = newBrand.id,
//                    serialType = "M",
//                )
//                firestoreHelper.addSmartphone(newSmartphone) { smartphoneId ->
//                    if (smartphoneId != null) {
//                        newSmartphone.id = smartphoneId
//                        Log.d("MainActivity", "Added smartphone: $newSmartphone")
//                    } else {
//                        Log.e("MainActivity", "Error adding smartphone")
//                    }
//                }
//            } else {
//                Log.e("MainActivity", "Error adding brand")
//            }
//        }

        // Fetch all brands and update the adapter with the list of brands
//        firestoreHelper.getAllBrands { brands ->
//            if (brands != null) {
//                adapter.updateData(brands)
//            } else {
//                Toast.makeText(this, "Error fetching brands", Toast.LENGTH_SHORT).show()
//            }
//        }

//    }

//        val brandId = "123"
//        smartphoneRecyclerView = findViewById(R.id.smartphoneRV)
//        smartphoneRecyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = SmartphoneAdapter(listOf()) // initialize the adapter with an empty list
//        smartphoneRecyclerView.adapter = adapter
//
//        firestoreHelper.getSmartphonesByBrandId(brandId) {smartphones ->
//            if (smartphones != null) {
//                adapter = SmartphoneAdapter(smartphones)
//                smartphoneRecyclerView.adapter = adapter
//            } else {
//                Toast.makeText(this, "Failed to fetch smartphones", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
    }
}
