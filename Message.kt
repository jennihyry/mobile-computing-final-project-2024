package com.example.mobilecomputingfinalproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    val content: String?,
    val author: String,
    val uri: String?,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
