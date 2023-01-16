package jpa

import dao.SmartphoneDAO
import entities.Smartphone
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImplementationSmartphoneDAO: SmartphoneDAO{
    /* Attributes */
    /* ---------------------------------------------- */
    private val mySmartphones = getSmartphonesFromCSV()

    /* Methods */
    /* ---------------------------------------------- */
    private fun getSmartphonesFromCSV(): ArrayList<Smartphone> {
        val inputStream = File("src/main/assets/SmartphonesData.csv")
        val reader = inputStream.bufferedReader()
        reader.readLine()

        val smartphones: List<Smartphone> = reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (id, serialType, model, brandId, price) = it.split(',', ignoreCase = false, limit = 5)

                Smartphone(
                    id.trim().toInt(),
                    serialType.trim()[0],
                    model.trim(),
                    brandId.trim().toInt(),
                    price.trim().toDouble()
                )
            }.toMutableList()

        return ArrayList(smartphones)
    }

     private fun OutputStream.saveSmartphonesOnCSV(smartphones: ArrayList<Smartphone>) {
        val writer = bufferedWriter()
        writer.write("""Id,Serial Type,Model,Brand Id,Price """)
        writer.newLine()
        smartphones.forEach {
            writer.write(
                "${it.getId()}," +
                    "${it.getSerialType()}," +
                    "${it.getModel()}," +
                    "${it.getBrandId()}," +
                    "${it.getPrice()}"
            )
            writer.newLine()
        }
        writer.flush()
    }

    override fun getSmartphonesByBrandId(brandId: Int): ArrayList<Smartphone> {
        val foundSmartphones = ArrayList<Smartphone>()

        mySmartphones.forEach { smartphone ->
            if(brandId == smartphone.getBrandId()){
                foundSmartphones.add(smartphone)
            }
        }

        return foundSmartphones
    }

    /* Gets all the smartphones */
    override fun getAllSmartphones(): ArrayList<Smartphone> {
        mySmartphones.forEachIndexed { index, smartphone ->
            println("$smartphone at index: $index")
        }

        return mySmartphones
    }

    /* Gets all the smartphones based on the given identifier */
    override fun getSmartphoneById(id: Int): Smartphone {
        var location = 0
        mySmartphones.forEachIndexed { index, smartphone ->
            if(id == smartphone.getId()){
                location = index
            }
        }

        return mySmartphones[location]
    }

    /* CRUD Operations */

    /* Creates a new smartphone */
    override fun create(entity: Smartphone) {
        val exists: Boolean = mySmartphones
            .any{smartphone ->
                return@any (smartphone.getId() == entity.getId())
            }

        if (!exists) {
            mySmartphones.add(entity)
            FileOutputStream("src/main/assets/SmartphonesData.csv").apply{
                saveSmartphonesOnCSV(mySmartphones)
            }
        } else {
            println("The smartphone already exists")
        }
    }

    /* Reads a smartphone based on the given id */
    override fun read(id: Int) {
        mySmartphones.forEachIndexed { index, smartphone ->
            if(id == smartphone.getId()){
                println(mySmartphones[index])
            }
        }
    }

    /* Updates a smartphone */
    override fun update(entity: Smartphone) {
        mySmartphones.forEachIndexed { index, smartphone ->
            smartphone.takeIf { it.getId() == entity.getId()}?.let {
                mySmartphones[index] = entity
            }
        }
        FileOutputStream("src/main/assets/SmartphonesData.csv").apply {
            saveSmartphonesOnCSV(mySmartphones)
        }
    }

    /* Deletes a smartphone based on its id */
    override fun delete(id: Int) {
        val exists: Boolean = mySmartphones
            .any{smartphone ->
                return@any (smartphone.getId() == id)
            }

        if (!exists) {
            println("Smartphone could not be removed, it does not exist")
        } else {
            mySmartphones.removeIf {it.getId() == id}

            FileOutputStream("src/main/assets/SmartphonesData.csv").apply {
                saveSmartphonesOnCSV(mySmartphones)
            }
        }
    }
}