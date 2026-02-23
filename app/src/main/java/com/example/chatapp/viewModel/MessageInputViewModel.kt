package com.example.chatapp.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.MessageClass
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.launch

class MessageInputViewModel : ViewModel() {

    private val _messageList = mutableStateListOf<MessageClass>()
    val messageList: List<MessageClass> get() = _messageList

    private val _messageSent = mutableStateOf(false)
    val messageSent: Boolean get() = _messageSent.value

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel("gemini-2.5-flash")

    fun addMessage(message: String) {

        if (message.isBlank()) return

        viewModelScope.launch {

            _messageList.add(MessageClass(message, "user"))

            _messageSent.value = true   // 🔥 notify UI

            try {
                val response = model.generateContent(message)
                val botReply = response.text ?: "No response"
                _messageList.add(MessageClass(botReply, "bot"))
            } catch (e: Exception) {
                _messageList.add(MessageClass("Error: ${e.message}", "bot"))
            }
        }
    }

    fun resetMessageSent() {
        _messageSent.value = false
    }
}
