package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.example.GeminiRepository // I need to create this

class ChatViewModel(private val repository: GeminiRepository) : ViewModel() {
    val messages = MutableStateFlow<List<String>>(emptyList())

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val response = repository.generateContent(message)
            messages.value = messages.value + listOf("User: $message", "IA: $response")
        }
    }
}
