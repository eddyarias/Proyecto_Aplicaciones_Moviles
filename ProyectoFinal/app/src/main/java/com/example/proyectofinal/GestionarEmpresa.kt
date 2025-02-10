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

class GestionarEmpresa : AppCompatActivity() {
    private var idEmpresa: Int = -1
    private lateinit var dbHelper: ESqliteHelperEmpresa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestionar_empresa)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = ESqliteHelperEmpresa(ESqliteHelper(this))

        // Referencias a los campos de entrada
        val tvCrearEmpresa = findViewById<TextView>(R.id.tvCrearEmpresa)
        val etRazonSocial = findViewById<EditText>(R.id.etRazonSocial)
        val etRuc = findViewById<EditText>(R.id.etRuc)
        val etNumeroSucursales = findViewById<EditText>(R.id.etNumSucursales)
        val etRepresentanteLegal = findViewById<EditText>(R.id.etRepresentanteLegal)
        val etActividadComercial = findViewById<EditText>(R.id.etActividadComercial)
        val btnGuardarEmpresa = findViewById<Button>(R.id.btnCrearEmpresa)

        // Recuperar el ID de la empresa (si se está editando)
        idEmpresa = intent.getIntExtra("ID_EMPRESA", -1)

        // Si ID_EMPRESA es válido, estamos editando una empresa existente
        if (idEmpresa != -1) {
            tvCrearEmpresa.text = "Actualizar Empresa"
            btnGuardarEmpresa.text = "Actualizar"

            // Consultar la base de datos para obtener los datos de la empresa
            val empresa = dbHelper.consultarEmpresaPorId(idEmpresa)
            empresa?.let {
                etRazonSocial.setText(it.razonSocial)
                etRuc.setText(it.ruc)
                etNumeroSucursales.setText(it.numeroSucursales.toString())
                etRepresentanteLegal.setText(it.representanteLegal)
                etActividadComercial.setText(it.actividadComercial)
            }
        } else {
            btnGuardarEmpresa.text = "Crear"
        }

        // Acción de crear o actualizar empresa
        btnGuardarEmpresa.setOnClickListener {
            val razonSocial = etRazonSocial.text.toString()
            val ruc = etRuc.text.toString()
            val numeroSucursalesStr = etNumeroSucursales.text.toString()
            val representanteLegal = etRepresentanteLegal.text.toString()
            val actividadComercial = etActividadComercial.text.toString()

            if (razonSocial.isNotEmpty() && ruc.isNotEmpty() && numeroSucursalesStr.isNotEmpty()
                && representanteLegal.isNotEmpty() && actividadComercial.isNotEmpty()
            ) {
                val numeroSucursales = numeroSucursalesStr.toIntOrNull()

                if (numeroSucursales != null) {
                    val respuesta = if (idEmpresa == -1) {
                        // Crear nueva empresa
                        dbHelper.crearEmpresa(razonSocial, ruc, numeroSucursales, representanteLegal, actividadComercial)
                    } else {
                        // Actualizar empresa existente
                        dbHelper.actualizarEmpresa(idEmpresa, razonSocial, ruc, numeroSucursales, representanteLegal, actividadComercial)
                    }

                    if (respuesta) {
                        mostrarSnackbar(if (idEmpresa == -1) "Empresa creada exitosamente" else "Empresa actualizada exitosamente")

                        // Regresar a la lista de empresas
                        val intent = Intent(this, Empresas::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        mostrarSnackbar("Error al guardar la empresa")
                    }
                } else {
                    mostrarSnackbar("El número de sucursales debe ser un número válido")
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