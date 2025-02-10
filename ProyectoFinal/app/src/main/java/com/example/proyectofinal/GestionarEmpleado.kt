package com.example.proyectofinal

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar


class GestionarEmpleado : AppCompatActivity() {
    private var idEmpleado: Int = -1
    private var idEmpresa: Int = -1
    private lateinit var dbHelper: ESqliteHelperEmpleado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestionar_empleado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = ESqliteHelperEmpleado(ESqliteHelper(this))

        // Referencias a los campos de entrada
        val tvGestionarEmpleado = findViewById<TextView>(R.id.tvCrearEmpleado)
        val etNombresCompletos = findViewById<EditText>(R.id.etNombresCompletos)
        val etCedula = findViewById<EditText>(R.id.etCedula)
        val etNumeroCelular = findViewById<EditText>(R.id.etCelular)
        val etFechaNacimiento = findViewById<EditText>(R.id.etFechaNacimiento)
        val spinnerPuesto = findViewById<Spinner>(R.id.spinnerPuesto)
        val btnGuardarEmpleado = findViewById<Button>(R.id.btnCrearEmpleado)

        // Llenar el spinner con los puestos
// Llenar el spinner con los puestos
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.puestos_array)) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).setTextColor(resources.getColor(android.R.color.white))  // Cambia el color del texto aquí
                return view
            }

        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPuesto.adapter = adapter

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPuesto.adapter = adapter

        // Recuperar el ID del empleado (si se está editando)
        idEmpleado = intent.getIntExtra("ID_EMPLEADO", -1)
        idEmpresa = intent.getIntExtra("ID_EMPRESA", -1)


        // Si ID_EMPLEADO es válido, estamos editando un empleado existente
        if (idEmpleado != -1) {
            tvGestionarEmpleado.text = "Actualizar Empleado"
            btnGuardarEmpleado.text = "Actualizar"

            // Consultar la base de datos para obtener los datos del empleado
            val empleado = dbHelper.consultarEmpleadoPorId(idEmpleado)
            empleado?.let {
                etNombresCompletos.setText(it.nombresCompletos)
                etCedula.setText(it.cedula)
                etNumeroCelular.setText(it.numeroCelular)
                etFechaNacimiento.setText(it.fechaNacimiento)
                // Si el puesto existe, seleccionamos el puesto en el spinner
                val puestoIndex = resources.getStringArray(R.array.puestos_array).indexOf(it.puesto)
                if (puestoIndex >= 0) {
                    spinnerPuesto.setSelection(puestoIndex)
                }
            }
        } else {
            btnGuardarEmpleado.text = "Crear"
        }

        // Acción de crear o actualizar empleado
        btnGuardarEmpleado.setOnClickListener {
            val nombresCompletos = etNombresCompletos.text.toString()
            val cedula = etCedula.text.toString()
            val numeroCelular = etNumeroCelular.text.toString()
            val fechaNacimiento = etFechaNacimiento.text.toString()
            val puesto = spinnerPuesto.selectedItem.toString()

            if (nombresCompletos.isNotEmpty() && cedula.isNotEmpty() && numeroCelular.isNotEmpty()
                && fechaNacimiento.isNotEmpty() && puesto.isNotEmpty()) {

                val respuesta = if (idEmpleado == -1) {
                    // Crear nuevo empleado
                    dbHelper.crearEmpleado(
                        nombresCompletos,
                        cedula,
                        numeroCelular,
                        fechaNacimiento,
                        puesto,
                        idEmpresa
                    )
                } else {
                    // Actualizar empleado existente
                    dbHelper.actualizarEmpleado(
                        idEmpleado,
                        nombresCompletos,
                        cedula,
                        numeroCelular,
                        fechaNacimiento,
                        puesto
                    )
                }

                if (respuesta) {
                    mostrarSnackbar(if (idEmpleado == -1) "Empleado creado exitosamente" else "Empleado actualizado exitosamente")

                    finish()
                } else {
                    mostrarSnackbar("Error al guardar el empleado")
                }
            } else {
                mostrarSnackbar("Por favor, completa todos los campos correctamente")
            }
        }
    }

    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.main), texto, Snackbar.LENGTH_LONG).show()
    }
}
