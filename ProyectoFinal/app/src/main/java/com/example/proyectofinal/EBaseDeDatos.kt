package com.example.proyectofinal

import android.content.Context

class EBaseDeDatos {
    companion object {
        var dbHelper: ESqliteHelper? = null
        var tablaEmpresa: ESqliteHelperEmpresa? = null
        var tablaEmpleado: ESqliteHelperEmpleado? = null

        fun inicializarBaseDeDatos(contexto: Context) {
            dbHelper = ESqliteHelper(contexto)
            tablaEmpleado = ESqliteHelperEmpleado(dbHelper!!)
            tablaEmpresa = ESqliteHelperEmpresa(dbHelper!!)
        }
    }
}