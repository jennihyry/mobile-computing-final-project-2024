package com.example.mobilecomputingfinalproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel(
    private val dao: MessageDao
): ViewModel() {
    val state = MutableStateFlow(MessageState())

    fun onEvent(event: MessageEvent){
        when(event){
            MessageEvent.LoadMessages -> {
                viewModelScope.launch {
                    val messages = dao.getAllMessages()
                    state.update{ it.copy(
                        messages = messages
                    )}
                }
            }

            is MessageEvent.SaveMessage -> {
                val content = state.value.content
                val author = state.value.author
                val uri = state.value.uri

                val message = Message(content, author, uri)

                viewModelScope.launch{
                    dao.upsertMessage(message)
                }

                state.update{ it.copy(
                    content = "",
                    author = "",
                    uri = null
                )}
            }

            is MessageEvent.UpdateMessageState -> {

                state.update{it.copy(
                    content = event.content,
                    author = event.author,
                    uri = event.uri
                )}
            }
        }
    }
}