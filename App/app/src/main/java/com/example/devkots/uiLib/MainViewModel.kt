package com.example.devkots.uiLib

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _greetingMessage = MutableStateFlow("Welcome to DevKots")
    val greetingMessage: StateFlow<String> = _greetingMessage

    fun updateGreeting(newMessage: String) {
        _greetingMessage.value = newMessage
    }
}