package com.example.devkots.uiLib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.RetrofitInstance
import com.example.devkots.util.HashUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userSessionViewModel: UserSessionViewModel // Dependency injection of UserSessionViewModel
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUsersByEmail(email.value)
                if (response.isSuccessful) {
                    val user = response.body()?.firstOrNull()
                    if (user != null) {
                        val hashedPassword = HashUtil.sha256(password.value)
                        if (user.password == hashedPassword) {
                            println("Login successful")
                            _loginSuccess.value = true
                            // Update UserSessionViewModel with user info, passing name, email, and biomonitorId
                            userSessionViewModel.loginUser(
                                name = user.name,
                                email = user.mail, // Pass the email here
                                biomonitorId = user.id.toString()
                            )
                        } else {
                            _errorMessage.value = "Invalid email or password"
                        }
                    } else {
                        _errorMessage.value = "User not found"
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}