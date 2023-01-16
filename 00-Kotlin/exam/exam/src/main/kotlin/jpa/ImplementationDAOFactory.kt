package jpa

import dao.BrandDAO
import dao.DAOFactory
import dao.SmartphoneDAO

class ImplementationDAOFactory: DAOFactory() {
    /* Methods */
    /* ---------------------------------------------- */
    override fun getSmartphoneDAO(): SmartphoneDAO {
        return ImplementationSmartphoneDAO()
    }

    override fun getBrandDAO(): BrandDAO {
        return ImplementationBrandDAO()
    }

}