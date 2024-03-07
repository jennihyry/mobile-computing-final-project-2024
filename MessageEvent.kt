package com.example.mobilecomputingfinalproject

sealed interface MessageEvent {
    data class UpdateMessageState(val content: String, val author: String, val uri: String?): MessageEvent
    object SaveMessage: MessageEvent
    object LoadMessages: MessageEvent
}