package dao

import entities.Brand

interface BrandDAO: GenericDAO<Brand, Int> {
    /* Methods */
    /* ---------------------------------------------- */
    fun getAllBrands(): ArrayList<Brand>

    fun getBrandsByName(name: String): ArrayList<Brand>

    fun getActiveBrands(): ArrayList<Brand>
}