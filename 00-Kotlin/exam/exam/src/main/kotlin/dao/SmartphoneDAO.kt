package dao

import entities.Smartphone

interface SmartphoneDAO: GenericDAO<Smartphone, Int> {
    /* Methods */
    /* ---------------------------------------------- */
    fun getSmartphonesByBrandName(brandName: String): ArrayList<Smartphone>

    fun getModels(): ArrayList<String>

    fun getSmartphoneById(id: Int): Smartphone
}