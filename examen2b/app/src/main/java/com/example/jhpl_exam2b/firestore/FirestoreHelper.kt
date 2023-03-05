package com.example.jhpl_exam2b.firestore

import android.content.ContentValues.TAG
import android.util.Log
import com.example.jhpl_exam2b.model.Brand
import com.example.jhpl_exam2b.model.Smartphone
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreHelper {
    /* Attributes */
    /* ---------------------------------------------- */
    private val db = Firebase.firestore

    /* Methods */
    /* ---------------------------------------------- */
    /* Brand CRUD methods */

    // CREATE a Brand on the brand FireStore database collection
    fun addBrand(brand: Brand, onComplete: (String?) -> Unit) {
        db.collection("brands")
            .add(brand)
            .addOnSuccessListener { documentReference ->
                val brandId = documentReference.id
                db.collection("brands").document(brandId)
                    .update("id", brandId)
                    .addOnSuccessListener {
//                        onComplete(true)
                        onComplete(documentReference.id)
                    }
                    .addOnFailureListener {
                        onComplete(null)
                    }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    // READ a brand with the given brandId from database collection
    fun getBrandById(brandId: String, onComplete: (Brand?) -> Unit) {
        db.collection("brands")
            .document(brandId)
            .get()
            .addOnSuccessListener { document ->
                val brand = document.toObject(Brand::class.java)
                brand?.id = document.id
                onComplete(brand)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting brand by ID: ${exception.message}")
                onComplete(null)
            }
    }

    // READ all the brands
    fun getAllBrands(onComplete: (List<Brand>?) -> Unit) {
        db.collection("brands")
            .get()
            .addOnSuccessListener { result ->
                val brands = mutableListOf<Brand>()
                for (document in result) {
                    val brand = document.toObject(Brand::class.java)
                    brand.id = document.id
                    brands.add(brand)
                }
                onComplete(brands)
            }
            .addOnFailureListener {exception ->
                Log.d(TAG, "Error getting brands: ${exception.message}")
                onComplete(emptyList())
            }
    }

    // UPDATE a Brand on the brand FireStore database collection
    fun updateBrand(brand: Brand, onComplete: (Boolean) -> Unit) {
        brand.id?.let { id ->
            db.collection("brands")
                .document(id)
                .set(brand)
                .addOnSuccessListener {
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        }
    }

    // DELETE a Brand on the brand FireStore database collection
    fun deleteBrand(brandId: String, onComplete: (Boolean) -> Unit) {
        db.collection("brands")
            .document(brandId)
            .delete()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    /* Smartphone CRUD methods */
    // CREATE a Smartphone on the smartphones database collection
    fun addSmartphone(smartphone: Smartphone, onComplete: (String?) -> Unit) {
        db.collection("brands").document(smartphone.brandId!!)
            .collection("smartphones")
            .add(smartphone)
            .addOnSuccessListener { documentReference ->
                val smartphoneId = documentReference.id
                db.collection("brands").document(smartphone.brandId!!)
                    .collection("smartphones").document(smartphoneId)
                    .update("id", smartphoneId)
                    .addOnSuccessListener {
                        onComplete(documentReference.id)
                    }
                    .addOnFailureListener {
                        onComplete(null)
                    }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    // READ Smartphones by the given brandId from the smartphones database collection
    fun getSmartphonesByBrandId(brandId: String, onComplete: (List<Smartphone>) -> Unit) {
        db.collection("brands")
            .document(brandId)
            .collection("smartphones")
            .get()
            .addOnSuccessListener { documents ->
                val smartphones = mutableListOf<Smartphone>()
                for (document in documents) {
                    val smartphone = document.toObject(Smartphone::class.java)
                    smartphones.add(smartphone)
                }
                onComplete(smartphones)
            }
            .addOnFailureListener {exception ->
                Log.d(TAG, "Error getting brands: ${exception.message}")
                onComplete(emptyList())
            }
    }

    // UPDATE a Smartphone based on the new data on the smartphones database collection
    fun updateSmartphone(smartphone: Smartphone, onComplete: (Boolean) -> Unit) {
        smartphone.id?.let { id ->
            db.collection("smartphones")
                .document(id)
                .set(smartphone)
                .addOnSuccessListener {
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        }
    }

    // DELETE a Smartphone based on the given smartphoneId
    fun deleteSmartphone(smartphoneId: String, brandId: String, onComplete: (Boolean) -> Unit) {
        db.collection("brands")
            .document(brandId)
            .collection("smartphones")
            .document(smartphoneId)
            .delete()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error deleting smartphone: ${exception.message}")
                onComplete(false)
            }
    }
}