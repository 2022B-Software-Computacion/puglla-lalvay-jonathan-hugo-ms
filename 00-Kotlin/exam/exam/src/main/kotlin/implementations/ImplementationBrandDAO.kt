package implementations

import dao.BrandDAO
import entities.Brand
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImplementationBrandDAO:  BrandDAO {
    /* Attributes */
    /* ---------------------------------------------- */
    private val myBrands = getBrandsFromCSV()

    /* Methods */
    /* ---------------------------------------------- */
    private fun getBrandsFromCSV(): ArrayList<Brand> {
        val inputStream = File("src/main/assets/BrandsData.csv")
        val reader = inputStream.bufferedReader()
        reader.readLine()

        val brands: List<Brand> = reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (id, name, price, status, hasWebpage) = it.split(',', ignoreCase = false, limit = 5)

                Brand(
                    id.trim().toInt(),
                    name.trim(),
                    price.trim().toDouble(),
                    status.trim()[0],
                    hasWebpage.trim().toBoolean()
                )
            }.toMutableList()

        return ArrayList(brands)
    }

    private fun OutputStream.saveBrandOnCSV(brands: ArrayList<Brand>) {
        val writer = bufferedWriter()
        writer.write("""id,name,price,status,has_webpage""")
        writer.newLine()
        brands.forEach {
            writer.write(
                "${it.getId()}," +
                        "${it.getName()}," +
                        "${it.getPrice()}," +
                        "${it.getStatus()}," +
                        "${it.getHasWebpage()}"
            )
            writer.newLine()
        }
        writer.flush()
    }

    /* Gets all the brands */
    override fun getAllBrands(): ArrayList<Brand> {
        return myBrands
    }

    /* Gets all the brands based on the given name */
    override fun getBrandByName(name: String): Brand {
        val brands = ArrayList<Brand>()

        myBrands.forEach { brand ->
            if(name.uppercase() == brand.getName().uppercase()){
                brands.add(brand)
            }
        }

        if (brands.isEmpty()) {
            brands.add(Brand(0, "null", 0, 'U', false))
        }

        return brands[0]
    }

    /* Gets all the active brands - A equals to an Active status */
    override fun getActiveBrands(): ArrayList<Brand> {
        val activeBrands = ArrayList<Brand>()

        myBrands.forEach { brand ->
            if('A' == brand.getStatus()){
                activeBrands.add(brand)
            }
        }

        return activeBrands
    }

    /* CRUD Operations */

    /* Creates a new brand */
    override fun create(entity: Brand) {
        val lastId: Int = myBrands.last().getId()

        val idExists: Boolean = myBrands
            .any{brand ->
                return@any (brand.getId() == entity.getId())
            }

        val nameExists: Boolean = myBrands
            .any{brand ->
                return@any (brand.getName().uppercase() == entity.getName().uppercase())
            }

        if (idExists || nameExists) {
            println("The brand already exists")
            return
        }

        println("Creating the Brand...")
        entity.setId(lastId + 1)
        myBrands.add(entity)
        FileOutputStream("src/main/assets/BrandsData.csv").apply{
            saveBrandOnCSV(myBrands)
        }
        println("Brand created successfully!")
    }

    /* Reads a brand based on the given id */
    override fun read(id: Int) {
        var exists = false

        myBrands.forEach { brand ->
            if(id == brand.getId()){
                println(brand)
                exists = true
            }
        }

        if(!exists){
            println("No data available")
            return
        }
    }

    /* Updates a brand */
    override fun update(entity: Brand) {
        var isAvailable = false

        myBrands.forEachIndexed { index, brand ->
            brand.takeIf { it.getId() == entity.getId()}?.let {
                myBrands[index] = entity
                isAvailable = true
            }
        }

        if(!isAvailable) {
            println("No data available to update")
            return
        }

        FileOutputStream("src/main/assets/BrandsData.csv").apply {
            saveBrandOnCSV(myBrands)
        }
    }

    /* Deletes a brand based on its id */
    override fun delete(id: Int) {
        var newId = 0
        val exists: Boolean = myBrands
            .any { brand ->
                return@any (brand.getId() == id)
            }

        if (!exists) {
            println("Brand could not be removed, it does not exist")
            return
        }

        myBrands.removeIf { it.getId() == id }
        myBrands.forEach { brand ->
            newId += 1
            brand.setId(newId)
        }
        FileOutputStream("src/main/assets/BrandsData.csv").apply {
            saveBrandOnCSV(myBrands)
        }
        println("Brand removed successfully")
    }
}