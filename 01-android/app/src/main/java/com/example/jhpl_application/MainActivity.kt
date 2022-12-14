package com.example.jhpl_application

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.AbsListView
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    val contenidoIntentExplicito =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    Log.i("intent-epn",
                        "${data?.getStringExtra("nombreModificado")}"
                    )
                }
            }
        }
    val contenidoIntentImplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        result ->
        if(result.resultCode == RESULT_OK) {
            if(result.data != null) {
                if (result.data!!.data != null) {
                    val uri: Uri = result.data!!.data!!
                    val cursor = contentResolver.query(
                        uri,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                    cursor?.moveToFirst()
                    val indiceTelefono = cursor?.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                    val telefono = cursor?.getString(
                        indiceTelefono!!
                    )
                    cursor?.close()
                    Log.i("intent-epn", "Teléfono ${telefono}")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botón del ciclo de vida
        val botonCicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        botonCicloVida
            .setOnClickListener {
                irActividad(ACicloVida::class.java)
            }

        // Botón del list view
        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonListView
            .setOnClickListener {
                irActividad(BListView::class.java)
            }

        val botonIntentImplicito = findViewById<Button>(R.id.btn_ir_intent_implicito)
        botonIntentImplicito
            .setOnClickListener {
                val intentConRespuesta = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                contenidoIntentImplicito.launch(intentConRespuesta)
            }

        val botonIntent = findViewById<Button>(R.id.btn_intent)
        botonIntent
            .setOnClickListener{
                abrirActividadConParametros(CIntentExplicitoParametros::class.java)
            }
    }

    fun abrirActividadConParametros(
        clase: Class<*>,
    ) {
        val intentExplicito = Intent(this, clase)
        // Enviar parámetros (solamente variables primitivas)
        intentExplicito.putExtra("nombre","Jonathan")
        intentExplicito.putExtra("apellido","Puglla")
        intentExplicito.putExtra("edad",22)
        intentExplicito.putExtra(
            "entrenadorPrincipal",
            BEntrenador(1, "Jonathan", "Paleta")
        )
        contenidoIntentExplicito.launch(intentExplicito)
    }
    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}

