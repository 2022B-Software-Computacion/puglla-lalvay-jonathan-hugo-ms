import dao.DAOFactory
import entities.Brand
import entities.Smartphone

fun main(){
    println("Smartphone execution")

    /* Calls the menu */
    menu()

    /* Prints the smartphones test */
    println("Getting all the smartphones")
    DAOFactory.getFactory().getSmartphoneDAO().getAllSmartphones()

    println("Getting the smartphone with the id 1")
    println(DAOFactory.getFactory().getSmartphoneDAO().getSmartphoneById(1))

    val smartphones = DAOFactory.getFactory().getSmartphoneDAO().getSmartphonesByBrandId(1)

    println("Getting the smartphone with the brand id 1")
    smartphones.forEach{smartphone ->
        println(smartphone)
    }

    println("Adding a Smartphone")
    val testSmartphone = Smartphone(4, 'F', "G419F", 2, 899.50)

    DAOFactory.getFactory().getSmartphoneDAO().create(testSmartphone)
    DAOFactory.getFactory().getSmartphoneDAO().getAllSmartphones()

    println("Reading a Smartphone")
    DAOFactory.getFactory().getSmartphoneDAO().read(3)

    println("Updating a Smartphone")
    val newTestSmartphone = Smartphone(4, 'F', "G419F", 2, 999.50)
    DAOFactory.getFactory().getSmartphoneDAO().update(newTestSmartphone)
    println("Updating a Smartphone: " + "${newTestSmartphone.getId()}")
    DAOFactory.getFactory().getSmartphoneDAO().read(newTestSmartphone.getId())

    println("Deleting a Smartphone")
    DAOFactory.getFactory().getSmartphoneDAO().delete(4)
    DAOFactory.getFactory().getSmartphoneDAO().getAllSmartphones()
    DAOFactory.getFactory().getSmartphoneDAO().delete(4)
    DAOFactory.getFactory().getSmartphoneDAO().getAllSmartphones()

    println("Brand execution")

    /* Calls the menu */
    menu()

    println("Getting all the brands")
    DAOFactory.getFactory().getBrandDAO().getAllBrands()

    println("Getting the brand with the name Apple")
    val brands = DAOFactory.getFactory().getBrandDAO().getBrandsByName("Apple")
    brands.forEach { brand ->
        println(brand)
    }

    println("Getting the brand with the active status")
    val activeBrands = DAOFactory.getFactory().getBrandDAO().getActiveBrands()
    activeBrands.forEach { brand ->
        println(brand)
    }

    println("Adding a Brand")
    val testBrand = Brand(4, "Sony", 299, 'B', false)

    DAOFactory.getFactory().getBrandDAO().create(testBrand)
    DAOFactory.getFactory().getBrandDAO().getAllBrands()

    println("Reading a Brand")
    DAOFactory.getFactory().getBrandDAO().read(3)

    println("Reading a fake Brand")
    DAOFactory.getFactory().getBrandDAO().read(6)

    println("Updating a Brand")
    val newTestBrand = Brand(4, "Sony", 299, 'B', true)
    DAOFactory.getFactory().getBrandDAO().update(newTestBrand)
    DAOFactory.getFactory().getBrandDAO().getAllBrands()

    println("Deleting a Brand")
    DAOFactory.getFactory().getBrandDAO().delete(4)
    DAOFactory.getFactory().getBrandDAO().getAllBrands()
}

fun menu() {
    println("------------------------------------------------------")
}