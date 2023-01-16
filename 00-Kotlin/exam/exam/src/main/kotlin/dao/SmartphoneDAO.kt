package dao

import entities.Smartphone

interface SmartphoneDAO: GenericDAO<Smartphone, Int> {
    /* Methods */
    /* ---------------------------------------------- */
    fun getSmartphonesByBrandId(brandId: Int): ArrayList<Smartphone>

    fun getAllSmartphones(): ArrayList<Smartphone>

    fun getSmartphoneById(id: Int): Smartphone
}