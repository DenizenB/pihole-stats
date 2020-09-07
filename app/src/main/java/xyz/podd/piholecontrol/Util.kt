package xyz.podd.piholecontrol

import java.security.MessageDigest

fun String.toSHA256(): String {
    val hash = MessageDigest.getInstance("SHA256").digest(this.toByteArray())
    return hash.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}