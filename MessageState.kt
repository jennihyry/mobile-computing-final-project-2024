package com.example.mobilecomputingfinalproject

data class MessageState(
    val messages: List<Message> = emptyList(),
    val content: String = "",
    val author: String = "",
    val uri: String? = ""
)
