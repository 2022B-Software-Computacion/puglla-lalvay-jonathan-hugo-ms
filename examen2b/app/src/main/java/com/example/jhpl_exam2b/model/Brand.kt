package com.example.jhpl_exam2b.model

data class Brand (
    /* Brand Data */
    /* ---------------------------------------------- */
    var id: String? = null,
    var name: String? = null,
    var price: Double? = null,
    // A -> Active, N -> New, B -> Banned
    var status: String? = null,
    var hasAWebPage: Boolean? = null
)