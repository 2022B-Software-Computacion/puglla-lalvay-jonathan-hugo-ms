package com.example.jhpl_exam2b.model

data class Brand (
    /* Brand Data */
    /* ---------------------------------------------- */
    var id: String? = null,
    var name: String? = null,
    var price: Double? = null,
    // Active, New, Banned
    var status: String? = null,
    var hasAWebPage: Boolean? = null
)