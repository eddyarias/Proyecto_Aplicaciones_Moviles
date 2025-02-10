package com.example.proyectofinal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelper(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "movilesProyecto", // Nombre único de la base de datos
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        // Script para crear la tabla EMPRESA
        val scriptSQLCrearTablaEmpresa = """
            CREATE TABLE EMPRESA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                razonSocial VARCHAR(100),
                ruc VARCHAR(20),
                numeroSucursales INTEGER,
                representanteLegal VARCHAR(100),
                actividadComercial VARCHAR(100)
            );
        """.trimIndent()

        // Script para crear la tabla EMPLEADO
        val scriptSQLCrearTablaEmpleado = """
            CREATE TABLE EMPLEADO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombresCompletos VARCHAR(100),
                cedula VARCHAR(20),
                numeroCelular VARCHAR(20),
                fechaNacimiento TEXT,
                puesto VARCHAR(50),
                empresaId INTEGER,
                FOREIGN KEY (empresaId) REFERENCES EMPRESA(id) ON DELETE CASCADE
            );
        """.trimIndent()

        // Ejecutando los scripts para crear las tablas
        db?.execSQL(scriptSQLCrearTablaEmpresa)
        db?.execSQL(scriptSQLCrearTablaEmpleado)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Si las tablas ya existen, las elimina y las vuelve a crear
        db?.execSQL("DROP TABLE IF EXISTS EMPLEADO")
        db?.execSQL("DROP TABLE IF EXISTS EMPRESA")
        onCreate(db)
    }
}
