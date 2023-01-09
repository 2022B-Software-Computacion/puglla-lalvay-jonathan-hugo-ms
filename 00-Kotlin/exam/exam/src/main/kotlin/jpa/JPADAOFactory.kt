package jpa

import dao.BrandDAO
import dao.DAOFactory
import dao.SmartphoneDAO

class JPADAOFactory: DAOFactory() {
    /* Methods */
    /* ---------------------------------------------- */
    override fun getSmartphoneDAO(): SmartphoneDAO {
        TODO("Not yet implemented")
    }

    override fun getBrandDAO(): BrandDAO {
        TODO("Not yet implemented")
    }

}