package dao

import jpa.JPADAOFactory

/* Attributes */
/* ---------------------------------------------- */
val factory: DAOFactory = JPADAOFactory()

abstract class DAOFactory {

    companion object {
        /* Methods */
        /* ---------------------------------------------- */
        fun getFactory(): DAOFactory {
            return factory
        }
    }

    abstract fun getSmartphoneDAO(): SmartphoneDAO

    abstract fun getBrandDAO(): BrandDAO
}
