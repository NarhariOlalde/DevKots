package com.example.devkots.uiLib.viewmodels

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.example.devkots.data.RetrofitInstance
import com.example.devkots.util.HashUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userSessionViewModel: UserSessionViewModel,
    private val auth0: Auth0
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
        val email = email.value
        val password = password.value

        loginWithUsernamePassword(
            auth0,
            email,
            password,
            onSuccess = { credentials ->
                // Decodificar el idToken para obtener información adicional como 'sub' y 'name'
                val idTokenPayload = decodeIdToken(credentials.idToken)
                val biomonitorId = idTokenPayload?.get("sub") as? String ?: ""  // El ID del usuario
                val name = idTokenPayload?.get("name") as? String ?: ""  // El nombre del usuario

                // Ahora puedes almacenar estos valores en el ViewModel
                userSessionViewModel.loginUser(
                    name = name,  // Usamos el 'name' de Auth0
                    email = email,
                    biomonitorId = biomonitorId,  // Usamos el 'sub' como biomonitorId
                    imageBase64 = "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"
                )
                _loginSuccess.value = true
            },
            onError = { error ->
                _errorMessage.value = error
            }
        )
    }

    private fun loginWithUsernamePassword(
        auth0: Auth0,
        username: String,
        password: String,
        onSuccess: (Credentials) -> Unit,
        onError: (String) -> Unit
    ) {

        val authentication = AuthenticationAPIClient(auth0)
        authentication
            .login(username, password, "Username-Password-Authentication")
            .setConnection("Username-Password-Authentication")
            .validateClaims()
            .setScope("openid profile email")  // Necesitamos los scopes openid, profile, y email
            .start(object : Callback<Credentials, AuthenticationException> {
                override fun onSuccess(result: Credentials) {
                    onSuccess(result)
                }

                override fun onFailure(error: AuthenticationException) {
                    Log.e("AuthError", "Error de autenticación: ${error.getDescription()}")
                    onError(error.message ?: "Error desconocido")
                }
            })
    }

    private fun decodeIdToken(idToken: String?): Map<String, Any>? {
        return try {
            idToken?.let {
                val parts = it.split(".")
                val payload = parts[1]
                val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE))

                val type = object : TypeToken<Map<String, Any>>() {}.type
                val gson = Gson()
                gson.fromJson<Map<String, Any>>(decodedPayload, type)
            }
        } catch (e: Exception) {
            Log.e("AuthError", "Error al decodificar el idToken: ${e.message}")
            null
        }
    }

}
