package com.example.savemypasswords.storage.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val number:String,
    val expiry:String,
    val cvc:String,
    val holder:String,
    @Transient
    override var id:Int = 0
): Identifiable
