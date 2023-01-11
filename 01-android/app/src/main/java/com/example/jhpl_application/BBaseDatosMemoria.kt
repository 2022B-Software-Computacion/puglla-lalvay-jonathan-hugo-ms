package com.example.jhpl_application

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador
                .add(
                    BEntrenador(1, "Jonathan", "j@j.com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador(2, "Hugo", "h@h.com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador(3, "Carolina", "c@c.com")
                )
        }
    }
}

