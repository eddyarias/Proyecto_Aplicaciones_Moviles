package com.example.proyectofinal

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView


class RVEmpleadoAdapter(private val empleados: List<BEmpleado>, private val onClick: (BEmpleado) -> Unit) :
    RecyclerView.Adapter<RVEmpleadoAdapter.EmpleadoViewHolder>() {

    // ViewHolder
    inner class EmpleadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val datosEmpleado: TextView = itemView.findViewById(R.id.datosEmpleado)
        val btnUpdate: ImageButton = itemView.findViewById(R.id.btn_actualizar_empleado)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_eliminar_empleado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpleadoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_empleado, parent, false)
        return EmpleadoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmpleadoViewHolder, position: Int) {
        val empleado = empleados[position]
        val context = holder.itemView.context

        holder.datosEmpleado.text = """
    ${empleado.nombresCompletos}
    ${empleado.puesto}
    ${empleado.cedula}
    ${empleado.numeroCelular}
    ${empleado.fechaNacimiento}
""".trimIndent()

        holder.btnUpdate.setOnClickListener {
            val intent = Intent(context, GestionarEmpleado::class.java)
            intent.putExtra("ID_EMPLEADO", empleado.id)  // Enviar ID de la tienda a editar
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            mostrarDialogoEliminar(empleado, context)
        }
    }

    // Función para mostrar el diálogo de confirmación de eliminación
    private fun mostrarDialogoEliminar(empleado: BEmpleado, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar empleado")
        builder.setMessage("¿Estás seguro de que deseas eliminar al empleado ${empleado.nombresCompletos}?")
        builder.setPositiveButton("Eliminar") { _, _ ->
            eliminarEmpleado(empleado, context)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    // Función que elimina el empleado de la base de datos y actualiza la lista
    private fun eliminarEmpleado(empleado: BEmpleado, context: Context) {
        val eliminado = EBaseDeDatos.tablaEmpleado?.eliminarEmpleado(empleado.id)
        if (eliminado == true) {
            // Si el empleado se eliminó correctamente de la BD
            Toast.makeText(context, "Empleado eliminado correctamente", Toast.LENGTH_SHORT).show()
            // Actualizamos la lista de empleados
            (context as Empleados).actualizarEmpleados(empleado.empresaId)
        } else {
            Toast.makeText(context, "Error al eliminar el empleado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return empleados.size
    }
}
