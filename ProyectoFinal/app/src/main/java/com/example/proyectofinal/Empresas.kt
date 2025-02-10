package com.example.proyectofinal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Empresas : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var empresaAdapter: RVEmpresaAdapter


    // Lista de tiendas
    var empresas = mutableListOf<BEmpresa>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_empresas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.empleadosList)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configuramos el Adapter
        empresaAdapter = RVEmpresaAdapter(empresas) { empresa ->
            Toast.makeText(this, "Ver empleados de ${empresa.razonSocial}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = empresaAdapter

        // cargar los datos desde la BD
        actualizarEmpresas()

        val backBoton = findViewById<ImageView>(R.id.backAEmpresas)
        backBoton
            .setOnClickListener {
                irActividad(MenuPrincipal::class.java)
            }

        val irCrearEmpresa = findViewById<ImageView>(R.id.btn_crear_empleado)
        irCrearEmpresa
            .setOnClickListener {
                irActividad(GestionarEmpresa::class.java)
            }
    }

    // Función para actualizar la lista de empresas
    public fun actualizarEmpresas() {
        empresas.clear() // Limpiar lista antes de cargar nuevos datos
        empresas.addAll(EBaseDeDatos.tablaEmpresa?.consultarTodasLasEmpresas()?: listOf()) // Obtener datos de la BD
        empresaAdapter.notifyDataSetChanged() // Notificar cambios al adapter
    }



    fun irActividad(clase:Class<*>){
        startActivity(Intent(this, clase))
    }

}