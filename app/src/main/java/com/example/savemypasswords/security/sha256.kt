package com.example.savemypasswords.security

import java.security.MessageDigest

private fun hashString(input: String, algorithm: String): ByteArray {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
}

fun String.sha256(): ByteArray {
    return hashString(this, "SHA-256")
}