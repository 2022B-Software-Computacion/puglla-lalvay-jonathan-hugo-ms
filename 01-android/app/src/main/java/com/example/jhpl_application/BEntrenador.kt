package com.example.jhpl_application

class BEntrenador (
    val nombre: String?,
    val descripcion: String?,
) {
    override fun toString(): String {
        return "Nombre: " + nombre + ", Descripción: " + descripcion
    }
}