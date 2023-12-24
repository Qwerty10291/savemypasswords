package com.example.savemypasswords.storage.models.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate

@Serializable
data class Note(
    val header:String,
    val content:String,
    @Serializable(with = LocalDateSerializer::class)
    val created: LocalDate,
    @Transient
    override var id:Int = 0
): Identifiable

