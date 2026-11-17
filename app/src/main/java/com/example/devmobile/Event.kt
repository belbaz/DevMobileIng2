package com.example.devmobile

// 'data class' is a special class used to keep data.
// Kotlin automatically generates methods like toString(), equals(), and copy()
data class Event(
    val id: Int,
    val title: String,
    val category: String,
    val location: String?,
    val capacity: Int,
    val description: String
)