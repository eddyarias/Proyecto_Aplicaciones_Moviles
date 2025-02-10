package com.example.proyectofinal

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar


class RVEmpresaAdapter(private val empresas: List<BEmpresa>, private val onClick: (BEmpresa) -> Unit) :
    RecyclerView.Adapter<RVEmpresaAdapter.EmpresaViewHolder>() {

    // ViewHolder
    inner class EmpresaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val companyName: TextView = itemView.findViewById(R.id.companyName)
        val btnViewEmployees: ImageButton = itemView.findViewById(R.id.btn_ver_empleados)
        val btnUpdate: ImageButton = itemView.findViewById(R.id.btn_actualizar_empresa)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_eliminar_empresa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpresaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_empresa, parent, false)
        return EmpresaViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: EmpresaViewHolder, position: Int) {
        val empresa = empresas[position]
        val context = holder.itemView.context

        holder.companyName.text = empresa.razonSocial

        holder.btnViewEmployees.setOnClickListener {
            val intent = Intent(context, Empleados::class.java)
            intent.putExtra("ID_EMPRESA", empresa.id)
            context.startActivity(intent)
        }

        holder.btnUpdate.setOnClickListener {
            val intent = Intent(context, GestionarEmpresa::class.java)
            intent.putExtra("ID_EMPRESA", empresa.id)  // Enviar ID de la tienda a editar
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            mostrarDialogoEliminar(empresa, context)
        }
    }

    // Función para mostrar el diálogo de confirmación de eliminación
    private fun mostrarDialogoEliminar(empresa: BEmpresa, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Eliminar empresa")
        builder.setMessage("¿Estás seguro de que deseas eliminar la empresa ${empresa.razonSocial}?")
        builder.setPositiveButton("Eliminar") { _, _ ->
            eliminarEmpresa(empresa, context)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }


    // Función que elimina la empresa de la base de datos y actualiza la lista
    private fun eliminarEmpresa(empresa: BEmpresa, context: Context) {
        val eliminado = EBaseDeDatos.tablaEmpresa?.eliminarEmpresa(empresa.id)
        if (eliminado == true) {
            // Si la empresa se eliminó correctamente de la BD
            Toast.makeText(context, "Empresa eliminada correctamente", Toast.LENGTH_SHORT).show()
            // Actualizamos la lista de empresas
            (context as Empresas).actualizarEmpresas()
        } else {
            Toast.makeText(context, "Error al eliminar la empresa", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return empresas.size
    }
}
