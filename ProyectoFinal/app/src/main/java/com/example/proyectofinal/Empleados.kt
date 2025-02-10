package com.example.proyectofinal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Empleados : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var empleadoAdapter: RVEmpleadoAdapter

    // Lista de empleados
    var empleados = mutableListOf<BEmpleado>()
    private var empresaId: Int = -1 // Añadido aquí para guardarlo como una variable de instancia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_empleados)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.empleadosList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configuramos el Adapter
        empleadoAdapter = RVEmpleadoAdapter(empleados) { empleado ->
            Toast.makeText(this, "Ver detalles de ${empleado.nombresCompletos}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = empleadoAdapter

        // Cargar los datos desde la BD
        empresaId = intent.getIntExtra("ID_EMPRESA", -1) // Guardar el ID de la empresa en la variable de instancia

        val tvInfoEmpresa = findViewById<TextView>(R.id.tvInfoEmpresa)

        val empresa = EBaseDeDatos.tablaEmpresa?.consultarEmpresaPorId(empresaId)

        empresa?.let {
            // Formateamos la información para mostrarla en el TextView
            val infoEmpresa = """
        Razón Social: ${it.razonSocial}
        RUC: ${it.ruc}
        Número de Sucursales: ${it.numeroSucursales}
        Representante Legal: ${it.representanteLegal}
        Actividad Comercial: ${it.actividadComercial}
    """.trimIndent()

            // Asignamos el texto formateado al TextView
            tvInfoEmpresa.text = infoEmpresa
        } ?: run {
            // Si la empresa no se encuentra, mostramos un mensaje de error
            tvInfoEmpresa.text = "Empresa no encontrada"
        }

        if (empresaId != -1) {
            actualizarEmpleados(empresaId)
        }

        val backBoton = findViewById<ImageView>(R.id.backAEmpresas)
        backBoton.setOnClickListener {
            irActividad(Empresas::class.java)
        }

        val irCrearEmpleado = findViewById<ImageView>(R.id.btn_crear_empleado)
        irCrearEmpleado.setOnClickListener {
            val intent = Intent(this, GestionarEmpleado::class.java)
            intent.putExtra("ID_EMPRESA", empresaId) // Enviar el ID de la empresa
            startActivity(intent)
        }
    }

    // Función para actualizar la lista de empleados de la empresa
    public fun actualizarEmpleados(empresaId: Int) {
        empleados.clear() // Limpiar lista antes de cargar nuevos datos
        empleados.addAll(EBaseDeDatos.tablaEmpleado?.consultarEmpleadosPorEmpresa(empresaId) ?: listOf()) // Obtener datos de la BD
        empleadoAdapter.notifyDataSetChanged() // Notificar cambios al adapter
    }

    // Función para navegar entre actividades
    private fun irActividad(clase: Class<*>) {
        startActivity(Intent(this, clase))
    }

    override fun onResume() {
        super.onResume()
        // Ahora solo llamamos a actualizarEmpleados sin pasar empresaId, ya que es una variable de instancia
        if (empresaId != -1) {
            actualizarEmpleados(empresaId)
        }
    }
}
