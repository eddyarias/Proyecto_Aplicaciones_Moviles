package com.example.proyectofinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val respuestaLoginUiAuth = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res: FirebaseAuthUIAuthenticationResult ->
        if (res.resultCode == RESULT_OK) {
            val usuario = FirebaseAuth.getInstance().currentUser
            if (usuario != null) {
                val nombreUsuario = usuario.displayName ?: "Usuario"
                irAMenuPrincipal(nombreUsuario) // Llamamos a la función para cambiar de pantalla
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.a_inicio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicializar BDD
        EBaseDeDatos.inicializarBaseDeDatos(this)

        val btnIngresar = findViewById<Button>(R.id.bt_ingresar)
        btnIngresar.setOnClickListener {
            iniciarSesion()
        }
    }


    private fun iniciarSesion() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build() // Inicio de sesión con Email
        )

        val logearseIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        respuestaLoginUiAuth.launch(logearseIntent)
    }

    private fun irAMenuPrincipal(nombre: String) {
        val intent = Intent(this, MenuPrincipal::class.java)
        intent.putExtra("NOMBRE_USUARIO", nombre)
        startActivity(intent)
        finish() // Cierra esta actividad para que el usuario no pueda volver atrás
    }
}