import dao.DAOFactory
import entities.Brand
import entities.Smartphone
import java.util.InputMismatchException
import java.util.Scanner
import kotlin.system.exitProcess

/* Global Attributes */
/* ---------------------------------------------- */
val reader = Scanner(System.`in`)
var option = 0
var innerOption = 0
fun main(){
    /* Attributes */
    /* ---------------------------------------------- */
    var myBrand: Brand
    var mySmartphone: Smartphone
    var brands: ArrayList<Brand>
    var smartphones: ArrayList<Smartphone>

    /* Implementation */
    /* ---------------------------------------------- */

    /* Calls the main menu until option value equals to the exit option */
    do {
        /* Calls the initial menu */
        mainMenu()
        option = getAValidNumber()
        println("*".repeat(83))

        when(option) {
            1 -> { // Brand class methods and CRUD operations are called based on the user's requirement
                brandsMenu()
                innerOption = getAValidNumber()
                println("*".repeat(83))
                when(innerOption) {
                    1 -> { // Prints all the brands available on te CSV document
                        println("Showing all the brands:")
                        brands = DAOFactory.getFactory().getBrandDAO().getAllBrands()
                        showBrands(brands)
                    }
                    2 -> { // Prints a brand based on the user input
                        print("What brand are you looking for? -> ")
                        val brandName = readLine()
                        myBrand = DAOFactory.getFactory().getBrandDAO().getBrandByName(brandName.toString())

                        // The brand could not be found
                        if (myBrand.getName() == "null") {
                            println("Brand does not exist")
                        }

                        if (myBrand.getName() != "null") {
                            brandsHeader()
                            println(myBrand)
                            println("-".repeat(77))
                            println()
                        }
                    }
                    3 -> { // Prints all the brands with an 'Active' status on the market
                        val activeBrands = DAOFactory.getFactory().getBrandDAO().getActiveBrands()

                        println("Getting the brands with an 'Active' status")
                        showBrands(activeBrands)
                    }
                    4 -> { // CREATE operation - Allows the user to add a new brand on the CSV data file
                        print("Brand's name: ")
                        val brandName = readLine().toString()

                        print("Brand's price: ")
                        val brandPrice = getAValidNumber()

                        println("Select the brand status: ")
                        var status = 'A'
                        brandsStatus()
                        option = getAValidNumber()
                        when(option){
                            1 -> status = 'A'
                            2 -> status = 'N'
                            3 -> status = 'B'
                            else -> println("Not a valid option")
                        }

                        println("Does the brand has a webpage?")
                        var hasWebpage = true
                        showWebOptions()
                        option = getAValidNumber()
                        when(option){
                            1 -> hasWebpage = true
                            2 -> hasWebpage = false
                            else -> println("Not a valid option")
                        }

                        myBrand = Brand(0, brandName, brandPrice, status, hasWebpage)
                        DAOFactory.getFactory().getBrandDAO().create(myBrand)
                    }
                    5 -> { // READ operation - Reads a brand based on the given id
                        print("Brand id -> ")
                        val brandId = getAValidNumber()
                        println("The brand you are looking for is: ")

                        brandsHeader()
                        DAOFactory.getFactory().getBrandDAO().read(brandId)
                        println("-".repeat(77))
                        println()
                    }
                    6 -> { // UPDATE operation - Updates a brand data based on the new data input
                        brands = DAOFactory.getFactory().getBrandDAO().getAllBrands()
                        showBrands(brands)

                        print("Brand id -> ")
                        val brandId = getAValidNumber()

                        myBrand = Brand(0, "Null", 0, 'A', false)
                        brands.forEach { brand ->
                            if(brandId == brand.getId()){
                                myBrand = brand
                            }
                        }

                        showBrandAttributes()
                        option = getAValidNumber()

                        when(option){
                            1 -> {
                                print("New Brand's name: ")
                                val brandName = readLine().toString()
                                myBrand.setName(brandName)
                            }
                            2 -> {
                                print("New Brand's price: ")
                                val brandPrice = getAValidNumber()
                                myBrand.setPrice(brandPrice)
                            }
                            3 -> {
                                println("Select the new brand status: ")
                                var status = 'A'
                                brandsStatus()
                                option = getAValidNumber()
                                when(option){
                                    1 -> status = 'A'
                                    2 -> status = 'N'
                                    3 -> status = 'B'
                                    else -> println("Not a valid option")
                                }
                                myBrand.setStatus(status)
                            }
                            4 -> {
                                println("Does the brand has a webpage now?")
                                var hasWebpage = true
                                showWebOptions()
                                option = getAValidNumber()
                                when(option){
                                    1 -> hasWebpage = true
                                    2 -> hasWebpage = false
                                    else -> println("Not a valid option")
                                }
                                myBrand.setHasWebpage(hasWebpage)
                            }
                        }

                        DAOFactory.getFactory().getBrandDAO().update(myBrand)
                        showBrands(brands)
                    }
                    7 -> { // DELETE operation -> Deletes a brand based on the user given id
                        print("Brand id -> ")
                        val brandId = getAValidNumber()
                        DAOFactory.getFactory().getBrandDAO().delete(brandId)
                    }
                    8 -> { // Go back to the last menu
                    }
                    else -> {
                        println("Given option is not valid")
                    }
                }
            }

            2 -> { // Smartphone class methods and CRUD operations are called based on the user's requirement
                smartphonesMenu()
                innerOption = getAValidNumber()
                println("*".repeat(83))

                when(innerOption) {
                    1 -> { // Prints all the smartphones available on the CSV document
                        println("Showing all the smartphones:")
                        smartphones = DAOFactory.getFactory().getSmartphoneDAO().getAllSmartphones()
                        showSmartphones(smartphones)
                    }
                    2 -> { // Gets a smartphone based on the given id
                        print("What smartphone id are you looking for? -> ")
                        val smartphoneId = Integer.valueOf(readLine())
                        mySmartphone = DAOFactory.getFactory().getSmartphoneDAO().getSmartphoneById(smartphoneId)

                        if(mySmartphone.getModel() == "null") {
                            println("Smartphone does not exist")
                        }

                        if(mySmartphone.getModel() != "null") {
                            smartphonesHeader()
                            println(mySmartphone)
                            println("-".repeat(77))
                            println()
                        }
                    }
                    3 -> { // Gets all the smartphones based on the given brand id
                        print("Insert the brand id -> ")
                        val brandId = Integer.valueOf(readLine())
                        smartphones = DAOFactory.getFactory().getSmartphoneDAO().getSmartphonesByBrandId(brandId)

                        if(smartphones[0].getModel() == "null") {
                            println("Brand id does not exist")
                        }

                        if(smartphones[0].getModel() != "null") {
                            showSmartphones(smartphones)
                        }

                    }
                    4 -> { // CREATE operation -> Creates a new smartphone based on the given data
                        println("Select a smartphone serial type: ")
                        var serialType = 'M'
                        showSerialTypes()
                        option = getAValidNumber()
                        when(option) {
                            1 -> serialType = 'M'
                            2 -> serialType = 'F'
                            3 -> serialType = 'N'
                            4 -> serialType = 'P'
                            else -> println("Not a valid option")
                        }

                        print("Smartphone's model: ")
                        val smartphoneModel = readLine().toString()

                        print("Smartphones's brand id: ")
                        val brandId = Integer.valueOf(readLine())

                        print("Smartphone's price: ")
                        val smartphonePrice = getAValidPrice()

                        mySmartphone = Smartphone(0, serialType, smartphoneModel, brandId, smartphonePrice)
                        DAOFactory.getFactory().getSmartphoneDAO().create(mySmartphone)
                    }
                    5 -> { // READ operation - Reads a smartphone based on the given id
                        print("Smartphone id -> ")
                        val smartphoneId = getAValidNumber()
                        println("The smartphone you are looking for is: ")

                        smartphonesHeader()
                        DAOFactory.getFactory().getSmartphoneDAO().read(smartphoneId)
                        println("-".repeat(77))
                        println()
                    }
                    6 -> { // UPDATE operation - Updates a smartphone data based on the new input
                        smartphones = DAOFactory.getFactory().getSmartphoneDAO().getAllSmartphones()
                        showSmartphones(smartphones)

                        println("NOTE: You are only allowed to change the smartphones price")
                        print("Smartphone id -> ")
                        val smartphoneId = getAValidNumber()

                        mySmartphone = Smartphone(0, 'N', "null", 0, 0.0)
                        smartphones.forEach { smartphone ->
                            if (smartphoneId == smartphone.getId()) {
                                mySmartphone = smartphone
                            }
                        }

                        print("New smartphone's price: ")
                        val smartphonePrice = getAValidPrice()
                        mySmartphone.setPrice(smartphonePrice)

                        DAOFactory.getFactory().getSmartphoneDAO().update(mySmartphone)
                        showSmartphones(smartphones)
                    }
                    7 -> { // DELETE operation -> Deletes a smartphone based on the user given id
                        print("Smartphone id -> ")
                        val smartphoneId = getAValidNumber()
                        DAOFactory.getFactory().getSmartphoneDAO().delete(smartphoneId)
                    }
                    8 -> { // Go back to the last menu
                    }
                    else -> {
                        println("Given option is not valid")
                    }
                }
            }
            3 -> exitProcess(0)
            else -> {
                println("Option is neither 1 nor 2")
            }
        }
    }while (option != 3)
}

/* Shows all the smartphones serial types available */
fun showSerialTypes() {
    val options = """
        1. Brand New
        2. Refurbished
        3. Replacement device
        4. Personalized device
    """.trimIndent()
    println(options)
    print("Insert your option -> ")
}

/* Methods */
/* ---------------------------------------------- */

/* Prints all the brads */
fun showBrands(myBrands: ArrayList<Brand>) {
    brandsHeader()
    myBrands.forEach { brands ->
        println("$brands")
    }
    println("-".repeat(77))
    println()
}

/* Prints all the brads */
fun showSmartphones(mySmartphones: ArrayList<Smartphone>) {
    smartphonesHeader()
    mySmartphones.forEach { smartphone ->
        println("$smartphone")
    }
    println("-".repeat(77))
    println()
}

/* Prints the main menu */
fun mainMenu() {
    println("*".repeat(83))
    val options = """
        MAIN MENU
        1. Brands access
        2. Smartphone access
        3. Exit
    """.trimIndent()
    println(options)
    print("Insert your option -> ")
}

/* Prints the brands menu */
fun brandsMenu() {
    println("*".repeat(83))
    val options = """
        BRANDS ACCESS
        1. Get all the brands
        2. Get a brand by its name
        3. Get all the active brands
        4. Insert a new brand
        5. Read a brand by its id
        6. Update a brand
        7. Delete a brand
        8. Go back
    """.trimIndent()
    println(options)
    print("Insert your option -> ")
}

/* Prints the available options for the status of a brand*/
fun brandsStatus() {
    val options = """
        1. Active
        2. New
        3. Banned
    """.trimIndent()
    println(options)
    print("Insert your option -> ")
}

fun showBrandAttributes() {
    val options = """
        1. Brand's name
        2. Brand's price
        3. Brand's status
        4. Brand's page status
    """.trimIndent()
    println(options)
    print("Insert your option -> ")
}

/* Prints the available options for the web status of a brand*/
fun showWebOptions() {
    val options = """
        1. Yes
        2. No
    """.trimIndent()
    println(options)
    print("Insert your option -> ")
}

/* Prints the smartphones menu */
fun smartphonesMenu() {
    println("*".repeat(83))
    val options = """
        SMARTPHONES ACCESS
        1. Get all the smartphones
        2. Get a smartphone by its id
        3. Get a smartphone by its brand id
        4. Insert a new smartphone
        5. Read a smartphone by its id
        6. Update a smartphone
        7. Delete a smartphone
        8. Go back
    """.trimIndent()
    println(options)
    print("Insert your option -> ")
}

/* Sets the brands table header */
fun brandsHeader() {
    println()
    println("-".repeat(77))
    println(
        String.format(
            "| %-8s | %-15s | %-15s | %-8s | %-15s |",
            "Id","Name","Price","Status","Has a website"
        )
    )
    println("-".repeat(77))
}

/* Sets the smartphones table header */
fun smartphonesHeader() {
    println()
    println("-".repeat(77))
    println(
        String.format(
            "| %-8s | %-15s | %-15s | %-8s | %-15s |",
            "Id","Serial Type","Model","Brand Id","Price"
        )
    )
    println("-".repeat(77))
}

/* Gets a valid number */
fun getAValidNumber(): Int {
    while (true) {
        try {
            val number = Integer.valueOf(readLine())
            assert(number >= 0) { "Not a valid number" }
            return number
        } catch (e: InputMismatchException) {
            reader.next()
            println("That's not a number. Try again:")
        }
    }
}

/* Gets a valid price */
fun getAValidPrice(): Double {
    while (true) {
        try {
            val price = reader.nextDouble()
            assert(price > 0) { "Can not read that price." }
            return price
        } catch (e: InputMismatchException) {
            reader.next()
            println("That's not a number. Try again:")
        }
    }
}