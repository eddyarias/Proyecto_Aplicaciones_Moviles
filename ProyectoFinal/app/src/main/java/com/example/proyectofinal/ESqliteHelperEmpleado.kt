package com.example.proyectofinal

import android.content.ContentValues

class ESqliteHelperEmpleado(private val dbHelper: ESqliteHelper) {

    //------------- CRUD ----------------------------

    // Crear empleado
    fun crearEmpleado(
        nombresCompletos: String,
        cedula: String,
        numeroCelular: String,
        fechaNacimiento: String,
        puesto: String,
        empresaId: Int
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresGuardar = ContentValues()

        valoresGuardar.put("nombresCompletos", nombresCompletos)
        valoresGuardar.put("cedula", cedula)
        valoresGuardar.put("numeroCelular", numeroCelular)
        valoresGuardar.put("fechaNacimiento", fechaNacimiento)
        valoresGuardar.put("puesto", puesto)
        valoresGuardar.put("empresaId", empresaId)

        val resultadoGuardar = baseDatosEscritura.insert("EMPLEADO", null, valoresGuardar)

        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    // Eliminar empleado
    fun eliminarEmpleado(id: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())

        val resultadoEliminar = baseDatosEscritura.delete(
            "EMPLEADO",
            "id=?",
            parametrosConsultaDelete
        )

        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    // Consultar todos los empleados
    fun consultarTodosLosEmpleados(): List<BEmpleado> {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = "SELECT * FROM EMPLEADO"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val arregloRespuesta = arrayListOf<BEmpleado>()

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val empleado = BEmpleado(
                    id = resultadoConsultaLectura.getInt(0),
                    nombresCompletos = resultadoConsultaLectura.getString(1),
                    cedula = resultadoConsultaLectura.getString(2),
                    numeroCelular = resultadoConsultaLectura.getString(3),
                    fechaNacimiento = resultadoConsultaLectura.getString(4),
                    puesto = resultadoConsultaLectura.getString(5),
                    empresaId = resultadoConsultaLectura.getInt(6)
                )
                arregloRespuesta.add(empleado)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        return arregloRespuesta
    }

    // Actualizar empleado
    fun actualizarEmpleado(
        id: Int,
        nombresCompletos: String,
        cedula: String,
        numeroCelular: String,
        fechaNacimiento: String,
        puesto: String,
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresAActualizar = ContentValues()

        valoresAActualizar.put("nombresCompletos", nombresCompletos)
        valoresAActualizar.put("cedula", cedula)
        valoresAActualizar.put("numeroCelular", numeroCelular)
        valoresAActualizar.put("fechaNacimiento", fechaNacimiento)
        valoresAActualizar.put("puesto", puesto)

        val parametrosConsultaActualizar = arrayOf(id.toString())

        val resultadoActualizar = baseDatosEscritura.update(
            "EMPLEADO",
            valoresAActualizar,
            "id=?",
            parametrosConsultaActualizar
        )

        baseDatosEscritura.close()
        return resultadoActualizar > 0
    }

    // Consultar empleado por ID
    fun consultarEmpleadoPorId(id: Int): BEmpleado? {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = "SELECT * FROM EMPLEADO WHERE id = ?"
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            val empleado = BEmpleado(
                id = resultadoConsultaLectura.getInt(0),
                nombresCompletos = resultadoConsultaLectura.getString(1),
                cedula = resultadoConsultaLectura.getString(2),
                numeroCelular = resultadoConsultaLectura.getString(3),
                fechaNacimiento = resultadoConsultaLectura.getString(4),
                puesto = resultadoConsultaLectura.getString(5),
                empresaId = resultadoConsultaLectura.getInt(6)
            )
            resultadoConsultaLectura.close()
            return empleado
        }
        resultadoConsultaLectura.close()
        return null
    }


    // Consultar empleados por ID de empresa con opciones de filtrado adicional
    fun consultarEmpleadosPorEmpresa(
        empresaId: Int,
        puesto: String? = null, // Parámetro opcional para filtrar por puesto
        ordenarPorNombre: Boolean = false // Opción para ordenar por nombre
    ): List<BEmpleado> {
        val baseDatosLectura = dbHelper.readableDatabase

        // Construcción de la consulta SQL con parámetros dinámicos
        var scriptConsultaLectura = "SELECT * FROM EMPLEADO WHERE empresaId = ?"
        val parametrosConsultaLectura = mutableListOf<String>().apply {
            add(empresaId.toString())
        }

        // Si se proporciona un filtro de puesto, lo agregamos a la consulta
        if (puesto != null && puesto.isNotEmpty()) {
            scriptConsultaLectura += " AND puesto = ?"
            parametrosConsultaLectura.add(puesto)
        }

        // Si se quiere ordenar por nombre, agregamos la cláusula ORDER BY
        if (ordenarPorNombre) {
            scriptConsultaLectura += " ORDER BY nombresCompletos ASC"
        }

        // Ejecución de la consulta con los parámetros construidos
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura.toTypedArray())

        val empleados = mutableListOf<BEmpleado>()

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val empleado = BEmpleado(
                    id = resultadoConsultaLectura.getInt(0),
                    nombresCompletos = resultadoConsultaLectura.getString(1),
                    cedula = resultadoConsultaLectura.getString(2),
                    numeroCelular = resultadoConsultaLectura.getString(3),
                    fechaNacimiento = resultadoConsultaLectura.getString(4),
                    puesto = resultadoConsultaLectura.getString(5),
                    empresaId = resultadoConsultaLectura.getInt(6)
                )
                empleados.add(empleado)
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        return empleados
    }
}
