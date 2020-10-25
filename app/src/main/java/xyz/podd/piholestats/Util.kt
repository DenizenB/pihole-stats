package xyz.podd.piholestats

import java.security.MessageDigest

fun String.toSHA256(): String {
    val hash = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return hash.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}