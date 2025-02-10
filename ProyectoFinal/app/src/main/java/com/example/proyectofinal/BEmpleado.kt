package com.example.proyectofinal

import android.os.Parcel
import android.os.Parcelable

data class BEmpleado(
    val id: Int,
    val nombresCompletos: String,
    val cedula: String,
    val numeroCelular: String,
    val fechaNacimiento: String,
    val puesto: String,
    val empresaId: Int
) : Parcelable {

    override fun toString(): String {
        return "$nombresCompletos - $puesto\nCédula: $cedula\nCelular: $numeroCelular\nFecha de Nacimiento: $fechaNacimiento"
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombresCompletos)
        parcel.writeString(cedula)
        parcel.writeString(numeroCelular)
        parcel.writeString(fechaNacimiento)
        parcel.writeString(puesto)
        parcel.writeInt(empresaId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BEmpleado> {
        override fun createFromParcel(parcel: Parcel): BEmpleado {
            return BEmpleado(parcel)
        }

        override fun newArray(size: Int): Array<BEmpleado?> {
            return arrayOfNulls(size)
        }
    }
}