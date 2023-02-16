package com.example.jhpl_application

class JCitiesDto (
    var name: String?,
    var state: String?,
    var country: String?,
    var capital: Boolean?,
    var population: Long?,
    var regions: List<String>?
    ){

    override fun toString(): String {
        return "$name - $country"
    }

}