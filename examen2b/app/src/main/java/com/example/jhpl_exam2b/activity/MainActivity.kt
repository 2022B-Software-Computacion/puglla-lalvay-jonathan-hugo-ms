package com.example.jhpl_exam2b.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jhpl_exam2b.R
import com.example.jhpl_exam2b.firestore.FireStoreHelper
import com.example.jhpl_exam2b.fragment.BrandListFragment
import com.example.jhpl_exam2b.model.Brand
import com.example.jhpl_exam2b.model.Smartphone

class MainActivity : AppCompatActivity() {
    /* Attributes */
    /* ---------------------------------------------- */
    private val fireStoreHelper = FireStoreHelper()
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

        val appleBrand = Brand(name = "Apple", price = 100000.0, status = "A", hasAWebPage = true)
        val samsungBrand = Brand(name = "Samsung", price = 200000.0, status = "A", hasAWebPage = true)
        val googleBrand = Brand(name = "Google", price = 300000.0, status = "A", hasAWebPage = true)
        brands.add(appleBrand)
        brands.add(samsungBrand)
        brands.add(googleBrand)

        fireStoreHelper.addBrand(appleBrand) { brandId ->
            if (brandId != null) {
                appleBrand.id = brandId
                Log.d("MainActivity", "Added brand: $appleBrand")

                // Create a new smartphone for the brand
                val mySmartphone = Smartphone(
                    modelName = "iPhone 14 Pro Max",
                    price = 1099.0,
                    brandId = appleBrand.id,
                    serialType = "M",
                )
                fireStoreHelper.addSmartphone(mySmartphone) { smartphoneId ->
                    if (smartphoneId != null) {
                        mySmartphone.id = smartphoneId
                        Log.d("MainActivity", "Added smartphone: $mySmartphone")
                    } else {
                        Log.e("MainActivity", "Error adding smartphone")
                    }
                }
            } else {
                Log.e("MainActivity", "Error adding brand")
            }
        }

        fireStoreHelper.addBrand(samsungBrand) { brandId ->
            if (brandId != null) {
                samsungBrand.id = brandId
                Log.d("MainActivity", "Added brand: $samsungBrand")

                // Create a new smartphone for the brand
                val mySmartphone = Smartphone(
                    modelName = "Samsung Galaxy S23 Ultra",
                    price = 1299.0,
                    brandId = samsungBrand.id,
                    serialType = "M",
                )
                fireStoreHelper.addSmartphone(mySmartphone) { smartphoneId ->
                    if (smartphoneId != null) {
                        mySmartphone.id = smartphoneId
                        Log.d("MainActivity", "Added smartphone: $mySmartphone")
                    } else {
                        Log.e("MainActivity", "Error adding smartphone")
                    }
                }
            } else {
                Log.e("MainActivity", "Error adding brand")
            }
        }

        fireStoreHelper.addBrand(googleBrand) { brandId ->
            if (brandId != null) {
                googleBrand.id = brandId
                Log.d("MainActivity", "Added brand: $googleBrand")

                // Create a new smartphone for the brand
                val mySmartphone = Smartphone(
                    modelName = "Google Pixel 6 Pro",
                    price = 900.0,
                    brandId = googleBrand.id,
                    serialType = "M",
                )
                fireStoreHelper.addSmartphone(mySmartphone) { smartphoneId ->
                    if (smartphoneId != null) {
                        mySmartphone.id = smartphoneId
                        Log.d("MainActivity", "Added smartphone: $mySmartphone")
                    } else {
                        Log.e("MainActivity", "Error adding smartphone")
                    }
                }
            } else {
                Log.e("MainActivity", "Error adding brand")
            }
        }

    }
}
