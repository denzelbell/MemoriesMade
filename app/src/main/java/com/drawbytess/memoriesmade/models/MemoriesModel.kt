package com.drawbytess.memoriesmade.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


// When using Parcelable it must be nullable so add ? marks
data class MemoriesModel (
    val id: Int,
    val title: String?,
    val image: String?,
    val description: String?,
    val date: String?,
    val location: String?,
    val longitude: Double,
    val latitude: Double
        ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(location)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemoriesModel> {
        override fun createFromParcel(parcel: Parcel): MemoriesModel {
            return MemoriesModel(parcel)
        }

        override fun newArray(size: Int): Array<MemoriesModel?> {
            return arrayOfNulls(size)
        }
    }
}