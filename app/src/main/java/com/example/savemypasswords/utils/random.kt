package com.example.savemypasswords.utils

import kotlin.random.Random

val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun RandomString(size:Int):String {
    return (1..size).map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
}