package com.example.jhpl_exam2b.model

data class Smartphone (
    /* Smartphone Data */
    /* ---------------------------------------------- */
    var id: String? = null,
    var modelName: String? = null,
    var price: Double? = null,
    /*
    * Brand New,
    * Refurbished,
    * Replacement device, and
    * Personalized device
    * */
    var serialType: String? = null,
    var brandId: String? = null
)