package xyz.podd.piholecontrol.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable
import xyz.podd.piholecontrol.service.ServiceHelper
import xyz.podd.piholecontrol.service.PiHoleService
import xyz.podd.piholecontrol.toSHA256

@Serializable
data class Device(val name: String, val url: String, val password: String, val verifySsl: Boolean = true): Parcelable {
    val authToken: String by lazy { password.toSHA256().toSHA256() }
    val service: PiHoleService by lazy { ServiceHelper.buildService(this) }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(null) as Boolean
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeString(password)
        parcel.writeValue(verifySsl)
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