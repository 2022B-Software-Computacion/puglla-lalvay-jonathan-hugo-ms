package com.example.jhpl_application

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelperEntrenador (
    contexto: Context?, // THIS
): SQLiteOpenHelper(
    contexto,
    "moviles", // nombre BDD
    null,
    1
){
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaEntrenador =
            """
                CREATE TABLE ENTRENADOR(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                descripcion VARCHAR(50)
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaEntrenador)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun crearEntrenador(
        nombre: String,
        descripcion: String
    ): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("descripcion", descripcion)
        val resultadoAGuardar = basedatosEscritura
            .insert(
                "ENTRENADOR", // Nombre tabla
            null,
                valoresAGuardar // valores
            )
        basedatosEscritura.close()
        return if(resultadoAGuardar.toInt() == -1) false else true
    }

    fun eliminarEntrenadorFormulario(id: Int): Boolean {
        // val conexionEscritura = this.writableDatabase
        val conexionEscritura = writableDatabase
//        "SELECT * FROM ENTRENADOR WHERE ID = ?"
//        arrayOf(
//            id.toString()
//        )
        val resultadoEliminacion = conexionEscritura
            .delete(
                "ENTRENADOR", // Nombre tabla
            "id=?", // Consulta Where
            arrayOf(
                id.toString()
            ) // Parámetros
            )
        conexionEscritura.close()
        return resultadoEliminacion.toInt() != -1
    }

    fun actualizarEntrenadorFormulario(
        nombre: String,
        descripcion: String,
        idActualizar: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)
        val resultadoActualizacion = conexionEscritura
            .update(
                "ENTRENADOR", // Nombre tabla
                valoresAActualizar, // Valores a actualizar
            "id=?", // Clausula Where
                arrayOf(
                    idActualizar.toString()
                ) // Parámetros cláusula Where
            )
        conexionEscritura.close()
        return resultadoActualizacion != -1
    }

    fun consultarEntrenadorPor(id: Int): BEntrenador {
        // val baseDatosLectura = this.readableDatabase
        val baseDatosLectura = readableDatabase
        val scriptConsultarUsuario = "SELECT * FROM ENTRENADOR WHERE ID = ?"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario, // Consulta
            arrayOf(
                id.toString()
            )   // Parámetros consulta
        )
        // Lógica búsqueda
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = BEntrenador(0, "", "")

        val arreglo = arrayListOf<BEntrenador>()
        // Lógica mientras se pueda mover al siguiente
        if(existeUsuario) {
            do {
                val id = resultadoConsultaLectura.getInt(0) // Columna indice 0 ID
                val nombre = resultadoConsultaLectura.getString(1) // Columna indice 1 NOMBRE
                val descripcion =
                    resultadoConsultaLectura.getString(2) // Columna indice 2 DESCRIPCION
                if (id != null){
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion = descripcion
                    arreglo.add(usuarioEncontrado)
                }
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }
}