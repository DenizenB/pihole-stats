package xyz.podd.piholestats.model.network

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
class Client(val name: String, val address: String): Parcelable {
    constructor(client: String): this(
        client.substringBefore("|"),
        client.substringAfter("|")
    )

    constructor(parcel: Parcel): this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun toString() = when (name.isEmpty()) {
        true -> address
        else -> name
    }

    override fun equals(other: Any?) = other is Client && address == other.address

    override fun hashCode() = address.hashCode()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Client> {
        override fun createFromParcel(parcel: Parcel): Client {
            return Client(parcel)
        }

        override fun newArray(size: Int): Array<Client?> {
            return arrayOfNulls(size)
        }
    }
}