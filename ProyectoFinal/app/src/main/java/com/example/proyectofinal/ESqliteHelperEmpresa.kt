package com.example.proyectofinal

import android.content.ContentValues

class ESqliteHelperEmpresa(private val dbHelper: ESqliteHelper) {

    //------------- CRUD ----------------------------

    // Crear empresa
    fun crearEmpresa(
        razonSocial: String,
        ruc: String,
        numeroSucursales: Int,
        representanteLegal: String,
        actividadComercial: String
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresGuardar = ContentValues()

        valoresGuardar.put("razonSocial", razonSocial)
        valoresGuardar.put("ruc", ruc)
        valoresGuardar.put("numeroSucursales", numeroSucursales)
        valoresGuardar.put("representanteLegal", representanteLegal)
        valoresGuardar.put("actividadComercial", actividadComercial)

        val resultadoGuardar = baseDatosEscritura.insert("EMPRESA", null, valoresGuardar)

        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    // Eliminar empresa
    fun eliminarEmpresa(id: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())

        val resultadoEliminar = baseDatosEscritura.delete(
            "EMPRESA",
            "id=?",
            parametrosConsultaDelete
        )

        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    // Consultar todas las empresas
    fun consultarTodasLasEmpresas(): List<BEmpresa> {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = "SELECT * FROM EMPRESA"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val arregloRespuesta = arrayListOf<BEmpresa>()

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val empresa = BEmpresa(
                    id = resultadoConsultaLectura.getInt(0),
                    razonSocial = resultadoConsultaLectura.getString(1),
                    ruc = resultadoConsultaLectura.getString(2),
                    numeroSucursales = resultadoConsultaLectura.getInt(3),
                    representanteLegal = resultadoConsultaLectura.getString(4),
                    actividadComercial = resultadoConsultaLectura.getString(5)
                )
                arregloRespuesta.add(empresa)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        return arregloRespuesta
    }

    // Actualizar empresa
    fun actualizarEmpresa(
        id: Int,
        razonSocial: String,
        ruc: String,
        numeroSucursales: Int,
        representanteLegal: String,
        actividadComercial: String
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresAActualizar = ContentValues()

        valoresAActualizar.put("razonSocial", razonSocial)
        valoresAActualizar.put("ruc", ruc)
        valoresAActualizar.put("numeroSucursales", numeroSucursales)
        valoresAActualizar.put("representanteLegal", representanteLegal)
        valoresAActualizar.put("actividadComercial", actividadComercial)

        val parametrosConsultaActualizar = arrayOf(id.toString())

        val resultadoActualizar = baseDatosEscritura.update(
            "EMPRESA",
            valoresAActualizar,
            "id=?",
            parametrosConsultaActualizar
        )

        baseDatosEscritura.close()
        return resultadoActualizar > 0
    }

    // Consultar empresa por ID
    fun consultarEmpresaPorId(id: Int): BEmpresa? {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = "SELECT * FROM EMPRESA WHERE id = ?"
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            val empresa = BEmpresa(
                id = resultadoConsultaLectura.getInt(0),
                razonSocial = resultadoConsultaLectura.getString(1),
                ruc = resultadoConsultaLectura.getString(2),
                numeroSucursales = resultadoConsultaLectura.getInt(3),
                representanteLegal = resultadoConsultaLectura.getString(4),
                actividadComercial = resultadoConsultaLectura.getString(5)
            )
            resultadoConsultaLectura.close()
            return empresa
        }
        resultadoConsultaLectura.close()
        return null
    }
}