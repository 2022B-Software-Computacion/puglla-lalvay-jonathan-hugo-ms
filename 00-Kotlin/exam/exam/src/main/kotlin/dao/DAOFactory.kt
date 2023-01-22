package dao

import implementations.ImplementationDAOFactory

abstract class DAOFactory {
    /* Attributes */
    /* ---------------------------------------------- */
    companion object {
        fun getFactory(): DAOFactory {
            return ImplementationDAOFactory()
        }
    }

    /* Methods */
    /* ---------------------------------------------- */
    abstract fun getSmartphoneDAO(): SmartphoneDAO

    abstract fun getBrandDAO(): BrandDAO
}
