package implementations

import dao.SmartphoneDAO
import entities.Smartphone
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImplementationSmartphoneDAO: SmartphoneDAO{
    /* Attributes */
    /* ---------------------------------------------- */
    private val mySmartphones = this.getSmartphonesFromCSV()

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
        val writer = this.bufferedWriter()
        writer.write("""id,serial_type,model,brand_id,price""")
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

        this.mySmartphones.forEach { smartphone ->
            if(brandId == smartphone.getBrandId()){
                foundSmartphones.add(smartphone)
            }
        }

        if (foundSmartphones.isEmpty()){
            foundSmartphones.add(
                Smartphone(0, 'N', "null", 0, 0.0)
            )
        }

        return foundSmartphones
    }

    /* Gets all the smartphones */
    override fun getAllSmartphones(): ArrayList<Smartphone> {
        return this.mySmartphones
    }

    /* Gets the smartphone based on the given identifier */
    override fun getSmartphoneById(id: Int): Smartphone {
        val smartphones = ArrayList<Smartphone>()

        this.mySmartphones.forEach { smartphone ->
            if(id == smartphone.getId()){
                smartphones.add(smartphone)
            }
        }

        if (smartphones.isEmpty()) {
            smartphones.add(
                Smartphone(0, 'N', "null", 0, 0.0)
            )
        }

        return smartphones[0]
    }

    /* CRUD Operations */

    /* Creates a new smartphone */
    override fun create(entity: Smartphone) {
        val lastId: Int = this.mySmartphones.last().getId()

        val idExists: Boolean = this.mySmartphones
            .any{smartphone ->
                return@any (smartphone.getId() == entity.getId())
            }

        val modelExists: Boolean = this.mySmartphones
            .any {smartphone ->
                return@any (smartphone.getModel().uppercase() == entity.getModel().uppercase())
            }

        if (idExists || modelExists) {
            println("The smartphone already exists")
            return
        }

        println("Creating the Smartphone...")
        entity.setId(lastId + 1)
        this.mySmartphones.add(entity)
        FileOutputStream("src/main/assets/SmartphonesData.csv").apply{
            this.saveSmartphonesOnCSV(this@ImplementationSmartphoneDAO.mySmartphones)
        }
        println("Smartphone created successfully!")
    }

    /* Reads a smartphone based on the given id */
    override fun read(id: Int) {
        var exists = false

        this.mySmartphones.forEach { smartphone ->
            if(id == smartphone.getId()){
                println(smartphone)
                exists = true
            }
        }

        if(!exists){
            println("The smartphone does not exist")
        }
    }

    /* Updates a smartphone */
    override fun update(entity: Smartphone) {
        var exists = false

        this.mySmartphones.forEachIndexed { index, smartphone ->
            smartphone.takeIf { it.getId() == entity.getId()}?.let {
                this.mySmartphones[index] = entity
                exists = true
            }
        }

        if (!exists) {
            println("No data available to update")
            return
        }

        FileOutputStream("src/main/assets/SmartphonesData.csv").apply {
            this.saveSmartphonesOnCSV(this@ImplementationSmartphoneDAO.mySmartphones)
        }
    }

    /* Deletes a smartphone based on its id */
    override fun delete(id: Int) {
        var newId = 0
        val exists: Boolean = this.mySmartphones
            .any{smartphone ->
                return@any (smartphone.getId() == id)
            }

        if (!exists) {
            println("Smartphone could not be removed, it does not exist")
        }

        this.mySmartphones.removeIf {it.getId() == id}
        this.mySmartphones.forEach { smartphone ->
            newId += 1
            smartphone.setId(newId)
        }

        FileOutputStream("src/main/assets/SmartphonesData.csv").apply {
            this.saveSmartphonesOnCSV(this@ImplementationSmartphoneDAO.mySmartphones)
        }
        println("Smartphone removed successfully")
    }
}