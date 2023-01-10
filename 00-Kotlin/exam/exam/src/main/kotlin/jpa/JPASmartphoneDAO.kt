package jpa

import dao.SmartphoneDAO
import entities.Brand
import entities.Smartphone
import java.io.InputStream

class JPASmartphoneDAO(): SmartphoneDAO{
    /* Constructor */
    /* ---------------------------------------------- */
    fun readCsv(inputStream: InputStream): List<Smartphone> {
        val reader = inputStream.bufferedReader()
        val header = reader.readLine()
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (model, price, brandId, id, firstSerialLetter) = it.split(',', ignoreCase = false, limit = 5)
                Smartphone(
                    model.trim().toString(),
                    price.trim().toDouble(),
                    brandId.trim().toString(),
                    id.trim().toInt(),
                    firstSerialLetter.trim()[0]
                )
            }.toList()
    }

    //val smartphones = readCsv(/*Open a stream to CSV file*/)

    /* Methods */
    /* ---------------------------------------------- */
    override fun getSmartphonesByBrandName(brandName: String): ArrayList<Smartphone> {
        TODO("Not yet implemented")
    }

    override fun getModels(): ArrayList<String> {
        TODO("Not yet implemented")
    }

    override fun getSmartphoneById(id: Int): Smartphone {
        TODO("Not yet implemented")
    }

    override fun create(entity: Smartphone) {
        TODO("Not yet implemented")
    }

    override fun read(id: Int) {
        TODO("Not yet implemented")
    }

    override fun update(entity: Smartphone) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Smartphone) {
        TODO("Not yet implemented")
    }
}