package dao

import entities.Brand

interface BrandDAO: GenericDAO<Brand, Int> {
    /* Methods */
    /* ---------------------------------------------- */
    fun getAllBrands(): ArrayList<Brand>

    fun getBrandByName(name: String): Brand

    fun getActiveBrands(): ArrayList<Brand>
}