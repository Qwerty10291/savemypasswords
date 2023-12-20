package com.example.savemypasswords.storage.models.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate

data class Note(
    val content:String,
    @Serializable(with = LocalDateSerializer::class)
    val created: LocalDate,
    @Transient
    override var id:Int = 0
): Identifiable

