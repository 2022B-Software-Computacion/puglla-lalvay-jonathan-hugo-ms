package dao

import entities.Brand

interface BrandDAO: GenericDAO<Brand, String> {
    /* Methods */
    /* ---------------------------------------------- */
    fun addValidBrand(status: Char): Unit
}