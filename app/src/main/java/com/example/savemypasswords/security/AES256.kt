package com.example.savemypasswords.security

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES256{
    private fun cipher(opmode:Int, secretKey:ByteArray):Cipher{
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val sk = SecretKeySpec(secretKey, "AES")
        val iv = IvParameterSpec(secretKey.sliceArray(0..15))
        c.init(opmode, sk, iv)
        return c
    }

    fun encrypt(data:ByteArray, secretKey:ByteArray):ByteArray{
        return cipher(Cipher.ENCRYPT_MODE, secretKey).doFinal(data)
    }

    fun decrypt(data:ByteArray, secretKey:ByteArray):ByteArray{
        return cipher(Cipher.DECRYPT_MODE, secretKey).doFinal(data)
    }

}