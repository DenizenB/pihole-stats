package xyz.podd.piholestats

import android.content.Context
import android.util.TypedValue
import java.security.MessageDigest

fun String.toSHA256(): String {
    val hash = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return hash.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

fun Number.dpToPx(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)