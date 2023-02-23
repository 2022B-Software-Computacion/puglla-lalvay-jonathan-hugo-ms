package com.example.jhpl_application


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.Query
import java.util.*
import kotlin.collections.ArrayList

class JFirebaseFirestore : AppCompatActivity() {
    var query: Query? = null
    val arreglo: ArrayList<JCitiesDto> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jfirebase_firestore)
        val listView = findViewById<ListView>(R.id.lv_firestore)
        val adaptador = ArrayAdapter(
            this, // Contexto
            android.R.layout.simple_list_item_1, // como se va a ver
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val botonDatosPrueba = findViewById<Button>(R.id.btn_fs_datos_prueba)
        botonDatosPrueba.setOnClickListener { crearDatosPrueba() }

        val botonOrderBy = findViewById<Button>(R.id.btn_fs_order_by)
        botonOrderBy.setOnClickListener { consultarConOrderBy(adaptador) }

        val botonObtenerDocumento = findViewById<Button>(R.id.btn_fs_odoc)
        botonObtenerDocumento.setOnClickListener{
            consultarDocumento(adaptador)
        }

        val botonFirebaseIndiceCompuesto = findViewById<Button>(R.id.btn_fs_ind_comp)
        botonFirebaseIndiceCompuesto.setOnClickListener{
            consultarIndiceCompuesto(adaptador)
        }

        val botonFirebaseCrear = findViewById<Button>(R.id.btn_fs_crear)
        botonFirebaseCrear.setOnClickListener {
            crearDatosEjemplo()
        }

        val botonFirebaseEliminar = findViewById<Button>(R.id.btn_fs_eliminar)
        botonFirebaseEliminar.setOnClickListener { eliminarRegistro() }

        val botonFirebaseEmpezarPaginar = findViewById<Button>(R.id.btn_fs_epaginar)
        botonFirebaseEmpezarPaginar.setOnClickListener { query = null; consultarCiudades(adaptador); }
    }

    fun consultarCiudades(adaptador: ArrayAdapter<JCitiesDto>){
        val db = Firebase.firestore
        val citiesRef = db.collection("cities").orderBy("population").limit(1)

        var tarea: Task<QuerySnapshot>? = null
        if (query == null) {
            tarea = citiesRef.get() // 1era
            limpiarArreglo()
            adaptador.notifyDataSetChanged()
        } else {
            tarea = query!!.get()
        }
    }

    // [1, 2, 3, 4, 5, 6, 7]
    // 4 primeros [1, 2, 3, 4]
    // [1, 3, 5, 6, 7, 10, 12]
    // 4 primeros [1, 3, 5, 6]
    // [1, 3, 5, 6, 7, 10, 12]
    // 4 primeros x => [1, 3, 5, 6]
    fun eliminarRegistro() {
        val db = Firebase.firestore
        val referenciaEjemploEstudiante = db
            .collection("ejemplo")
            .document("123456789")
            .collection("estudiante")

        referenciaEjemploEstudiante
            .document("123456789")
            .delete()
            .addOnCompleteListener { /* Si todo salio bien */ }
            .addOnFailureListener { /* Si algo salio mal */ }
    }

    fun crearDatosEjemplo(){
        val db = Firebase.firestore
        val referenciaEjemploEstudiante = db
            .collection("ejemplo") // ejemplo/123456789/estudiante/...
            .document("123456789")
            .collection("estudiante")
        val identificador = Date().time
        val datosEstudiante = hashMapOf(
            "nombre" to "Jonathan",
            "graduado" to false,
            "premedio" to false,
            "direccion" to hashMapOf(
                "direccion" to "Mitad del mundo",
                "numeroCalle" to 1234
            ),
            "materias" to listOf("web", "moviles")
        )

        // Con identificador quemado
        referenciaEjemploEstudiante
            .document("123456789")
            .set(datosEstudiante) // actualiza o crea
            .addOnCompleteListener {/* Si todo salio bien */ }
            .addOnFailureListener{ /*Si algo salio mal */ }

        // Con identificador generado en Date.time
        referenciaEjemploEstudiante
            .document(identificador.toString())
            .set(datosEstudiante) // actualiza o crea
            .addOnCompleteListener {/* Si todo salio bien */ }
            .addOnFailureListener{ /*Si algo salio mal */ }

        // Sin identificador
        referenciaEjemploEstudiante
            .add(datosEstudiante) // crea
            .addOnCompleteListener {/* Si todo salio bien */ }
            .addOnFailureListener{ /*Si algo salio mal */ }
    }

    fun consultarIndiceCompuesto(adaptador: ArrayAdapter<JCitiesDto>) {
        val db = Firebase.firestore
        val citiesRefUnico = db.collection("cities")
        citiesRefUnico
            .whereEqualTo("capital", false)
            .whereLessThanOrEqualTo("population", 4000000)
            .orderBy("population",
                com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (ciudad in it) {
                    anadirAArregloCiudad(arreglo, ciudad, adaptador)
                }
            }
    }

    fun  consultarConOrderBy(
        adaptador: ArrayAdapter<JCitiesDto>
    ) {
        val db = Firebase.firestore
        val citiesRefUnico = db.collection("cities")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()
        citiesRefUnico
        // No USAMOS CON DOCUMENT porque document nos devuelve 1
        // /cities => "population" ASCENDING
            .orderBy("population", com.google.firebase.firestore.Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener {
                for(ciudad in it) {
                    anadirAArregloCiudad(arreglo, ciudad, adaptador)
                }
            }
    }

    fun consultarDocumento( adaptador: ArrayAdapter<JCitiesDto> ) {
        val db = Firebase.firestore
        val citiesRefUnico = db
            .collection("cities")
        // /cities/BJ (1 documento)
        // /cities/BG/hijo/1/nieto/1.1

        citiesRefUnico
            .document("BJ")
//            .collection("hijo")
//            .document("1")
//            .collection("nieto")
//            .document("1.1")
            .get()
            .addOnSuccessListener {
                it.id // obtener el id de firestore
                limpiarArreglo()
                arreglo.add(
                    JCitiesDto(
                        it.data?.get("name") as String?,
                        it.data?.get("state") as String?,
                        it.data?.get("country") as String?,
                        it.data?.get("capital") as Boolean?,
                        it.data?.get("population") as Long?,
                        it.data?.get("regions") as ArrayList<String>
                    )
                )
                adaptador.notifyDataSetChanged()
            }
    }
    private fun crearDatosPrueba() {
        val db = Firebase.firestore // objeto Firestore
        val cities = db.collection("cities") // nombre de la colección

        val data1 = hashMapOf(  // objeto a guardarse
            "name" to "San Francisco",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 860000,
            "regions" to listOf("west_coast", "norcal")
        )
        cities.document("SF").set(data1)

        val data2 = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 3900000,
            "regions" to listOf("west_coast", "socal")
        )
        cities.document("LA").set(data2)

        val data3 = hashMapOf(
            "name" to "Washington D.C.",
            "state" to null,
            "country" to "USA",
            "capital" to true,
            "population" to 680000,
            "regions" to listOf("east_coast")
        )
        cities.document("DC").set(data3)

        val data4 = hashMapOf(
            "name" to "Tokyo",
            "state" to null,
            "country" to "Japan",
            "capital" to true,
            "population" to 9000000,
            "regions" to listOf("kanto", "honshu")
        )
        cities.document("TOK").set(data4)

        val data5 = hashMapOf(
            "name" to "Beijing",
            "state" to null,
            "country" to "China",
            "capital" to true,
            "population" to 21500000,
            "regions" to listOf("jingjinji", "hebei")
        )
        cities.document("BJ").set(data5)
    }

    fun limpiarArreglo() { arreglo.clear() }
    fun anadirAArregloCiudad(
        arregloNuevo: ArrayList<JCitiesDto>,
        ciudad: QueryDocumentSnapshot,
        adaptador: ArrayAdapter<JCitiesDto>
    ) {
        val nuevaCiudad = JCitiesDto(
            ciudad.data["name"] as String?,
            ciudad.data["state"] as String?,
            ciudad.data["country"] as String?,
            ciudad.data["capital"] as Boolean?,
            ciudad.data["population"] as Long?,
            ciudad.data["regions"] as ArrayList<String>?,
        )

        arregloNuevo.add(
            nuevaCiudad
        )
        adaptador.notifyDataSetChanged()
    }

}