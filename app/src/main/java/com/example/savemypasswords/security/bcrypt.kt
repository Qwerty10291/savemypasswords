package com.example.savemypasswords.security

import org.mindrot.jbcrypt.BCrypt

object BCRYPT {
    fun HashPassword(password:String):String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun ValidatePasswordHash(password:String, hash:String):Boolean {
        return BCrypt.checkpw(password, hash)
    }
}