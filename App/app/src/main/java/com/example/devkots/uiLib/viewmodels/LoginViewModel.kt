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
                    imageBase64 = null  // Si tienes una imagen en base64, puedes agregarla aquí
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
