package com.example.savemypasswords.security

import java.security.MessageDigest

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }
}

fun String.sha256(): String {
    return hashString(this, "SHA-256")
}