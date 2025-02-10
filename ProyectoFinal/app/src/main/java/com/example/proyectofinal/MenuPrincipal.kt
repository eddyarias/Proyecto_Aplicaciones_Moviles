package com.example.proyectofinal

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener el nombre del usuario desde el Intent
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")

        // Encontrar el TextView donde se mostrará el nombre
        val tvUserName = findViewById<TextView>(R.id.userName)

        // Si el nombre no es nulo, actualizar el texto del TextView
        if (nombreUsuario != null) {
            tvUserName.text = "$nombreUsuario"
        } else {
            tvUserName.text = "Usuario"
        }

        // Obtener las referencias de los elementos del layout
        val manageEmpresasLayout = findViewById<LinearLayout>(R.id.menuPanel).findViewById<LinearLayout>(R.id.manageCompaniesButton)

        // Configurar los listeners para los clics
        manageEmpresasLayout.setOnClickListener {
            val intent = Intent(this, Empresas::class.java)
            startActivity(intent)
        }
    }
}