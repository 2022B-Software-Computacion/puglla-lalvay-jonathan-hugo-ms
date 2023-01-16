package jpa

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
        writer.write("""Id,Name,Price,Status,Webpage""")
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
        myBrands.forEachIndexed { index, brands ->
            println("$brands at index: $index")
        }

        return myBrands
    }

    /* Gets all the brands based on the given name */
    override fun getBrandsByName(name: String): ArrayList<Brand> {
        val foundBrands = ArrayList<Brand>()

        myBrands.forEach { brand ->
            if(name == brand.getName()){
                foundBrands.add(brand)
            }
        }

        return foundBrands
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
        val exists: Boolean = myBrands
            .any{brand ->
                return@any (brand.getId() == entity.getId())
            }

        if (!exists) {
            myBrands.add(entity)
            FileOutputStream("src/main/assets/BrandsData.csv").apply{
                saveBrandOnCSV(myBrands)
            }
        } else {
            println("The brand already exists")
        }
    }

    /* Reads a brand based on the given id */
    override fun read(id: Int) {
        myBrands.forEachIndexed { index, brand ->
            if(id == brand.getId()){
                println(myBrands[index])
            }
        }
    }

    /* Updates a brand */
    override fun update(entity: Brand) {
        myBrands.forEachIndexed { index, brand ->
            brand.takeIf { it.getId() == entity.getId()}?.let {
                myBrands[index] = entity
            }
        }

        FileOutputStream("src/main/assets/BrandsData.csv").apply {
            saveBrandOnCSV(myBrands)
        }
    }

    /* Deletes a brand based on its id */
    override fun delete(id: Int) {
        val exists: Boolean = myBrands
            .any{brand ->
                return@any (brand.getId() == id)
            }

        if (!exists) {
            println("Brand could not be removed, it does not exist")
        } else {
            myBrands.removeIf {it.getId() == id}

            FileOutputStream("src/main/assets/BrandsData.csv").apply {
                saveBrandOnCSV(myBrands)
            }
        }
    }
}