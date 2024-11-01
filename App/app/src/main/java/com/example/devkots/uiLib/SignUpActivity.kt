package com.example.devkots.uiLib

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.devkots.R
import com.example.devkots.api.ApiService
import com.example.devkots.api.RetrofitClient
import com.example.devkots.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameInput = findViewById(R.id.name_input)
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        signUpButton = findViewById(R.id.signup_button)

        signUpButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val encryptedPassword = hashPassword(password)
                createUser(name, email, encryptedPassword)
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun createUser(name: String, email: String, password: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newUser = User(id = 0, name = name, mail = email, password = password) // Assuming `id` is auto-generated
                apiService.createUser(newUser) // Send POST request to create user

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignUpActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    navigateToDashboard(newUser)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignUpActivity, "Failed to create account", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToDashboard(user: User) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("USERNAME", user.name) // Pass the username to DashboardActivity
        startActivity(intent)
        finish() // Close SignUpActivity to prevent going back to it
    }
}