package jpa

import dao.BrandDAO
import entities.Brand

class JPABrandDAO(): JPAGenericDAO<Brand, String>(), BrandDAO {
    /* Methods */
    /* ---------------------------------------------- */
    override fun addValidBrand(status: Char) {
        TODO("Not yet implemented")
    }
}