package com.example.jhpl_application

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador
                .add(
                    BEntrenador("Jonathan", "j@j.com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador("Hugo", "h@h.com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador("Carolina", "c@c.com")
                )
        }
    }
}

