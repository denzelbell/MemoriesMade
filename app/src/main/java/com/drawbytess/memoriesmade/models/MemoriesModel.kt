package com.drawbytess.memoriesmade.models

data class MemoriesModel (
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val date: String,
    val location: String,
    val longitude: Double,
    val latitude: Double
        )