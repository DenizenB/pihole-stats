package xyz.podd.piholestats

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import xyz.podd.piholestats.model.Device

private const val PREFERENCE_FILE = "prefs"

class Storage(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)

    var devices: MutableList<Device>
        get() = get(DEVICES) ?: ArrayList()
        set(it) = put(DEVICES, it)

    private inline fun <reified T> get(key: String): T? {
        val encoded = preferences.getString(key, null)
        return when (encoded) {
            null -> null
            else -> Json.decodeFromString(encoded)
        }
    }

    private inline fun <reified T> put(key: String, value: T) =
        preferences.edit().putString(key, Json.encodeToString(value)).apply()

    companion object {
        const val DEVICES = "devices"
    }
}