package xyz.podd.piholecontrol.model

import android.os.Parcel
import android.os.Parcelable
import xyz.podd.piholecontrol.toSHA256

data class Device(val name: String, val url: String, val password: String): Parcelable {
    val authToken: String by lazy { password.toSHA256().toSHA256() }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeString(password)
    }

    companion object CREATOR : Parcelable.Creator<Device> {
        override fun createFromParcel(parcel: Parcel): Device {
            return Device(parcel)
        }

        override fun newArray(size: Int): Array<Device?> {
            return arrayOfNulls(size)
        }
    }
}