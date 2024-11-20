package com.example.devkots.uiLib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.RetrofitInstance
import com.example.devkots.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserSessionViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _biomonitorId = MutableStateFlow("")
    val biomonitorId: StateFlow<String> = _biomonitorId

    private val temp:String? = null
    private val _imageBase64 = MutableStateFlow(temp)
    val imageBase64: StateFlow<String?> = _imageBase64

    private val _updateStatus = MutableStateFlow<Boolean?>(null) // Success status of update action
    val updateStatus: StateFlow<Boolean?> = _updateStatus

    /**
     * Logs in the user by setting their info
     */
    fun loginUser(name: String, email: String, biomonitorId: String, imageBase64: String?) {
        _userName.value = name
        _email.value = email
        _biomonitorId.value = biomonitorId
        _isLoggedIn.value = true
        _imageBase64.value = imageBase64
    }

    /**
     * Updates the user's info in the session. This is typically called when user updates their profile.
     */
    fun updateUserInfo(name: String, email: String, password: String, imageBase64: String?) {
        val id = biomonitorId.value.toIntOrNull() ?: return  // Ensure we have a valid ID

        viewModelScope.launch {
            val user = User(id = id, name = name, mail = email, password = password, imageBase64 = imageBase64)
            try {
                val response = RetrofitInstance.api.updateUser(id, user)
                if (response.isSuccessful) {
                    _userName.value = name
                    _email.value = email
                    _updateStatus.value = true
                    _imageBase64.value = imageBase64
                } else {
                    _updateStatus.value = false
                }
            } catch (e: Exception) {
                _updateStatus.value = false
            }
        }
    }
    /**
     * Logs out the user by clearing session data.
     */
    fun logoutUser() {
        _userName.value = ""
        _email.value = ""
        _biomonitorId.value = ""
        _isLoggedIn.value = false
        _imageBase64.value = ""
    }
}