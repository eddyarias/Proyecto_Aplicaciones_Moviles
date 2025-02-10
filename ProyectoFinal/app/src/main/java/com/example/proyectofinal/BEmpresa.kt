package com.example.proyectofinal

import android.os.Parcel
import android.os.Parcelable

data class BEmpresa(
    val id: Int,
    val razonSocial: String,
    val ruc: String,
    val numeroSucursales: Int,
    val representanteLegal: String,
    val actividadComercial: String
) : Parcelable {

    override fun toString(): String {
        return "$razonSocial - $actividadComercial\nRUC: $ruc\nNúmero de Sucursales: $numeroSucursales\nRepresentante Legal: $representanteLegal"
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(razonSocial)
        parcel.writeString(ruc)
        parcel.writeInt(numeroSucursales)
        parcel.writeString(representanteLegal)
        parcel.writeString(actividadComercial)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BEmpresa> {
        override fun createFromParcel(parcel: Parcel): BEmpresa {
            return BEmpresa(parcel)
        }

        override fun newArray(size: Int): Array<BEmpresa?> {
            return arrayOfNulls(size)
        }
    }
}