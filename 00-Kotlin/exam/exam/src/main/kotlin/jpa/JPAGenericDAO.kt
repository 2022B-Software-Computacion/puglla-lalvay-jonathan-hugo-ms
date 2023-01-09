package jpa

import dao.GenericDAO
import java.io.File
import java.io.InputStream

open class JPAGenericDAO<T, ID> : GenericDAO<T, ID> {
    /* CRUD Methods */
    /* ---------------------------------------------- */
    override fun create(entity: T) {
        val inputStream: InputStream
        val reader = inputStream.bufferedReader()
        val header = reader.readLine()
        return reader.lineSequence().filter { it.isNotBlank() }
            .map {
                val()
            }

    }

    override fun read(id: ID) {
        val file = File("input"+File.separator+"contents.txt")
        val bufferedReader = file.bufferedReader( )
        val text: List<String> = bufferedReader.readLines()

        for(line in text){
            println(line)
        }
    }

    override fun update(entity: T) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: T) {
        TODO("Not yet implemented")
    }
}