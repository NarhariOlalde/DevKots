package com.example.devkots

import androidx.test.platform.app.InstrumentationRegistry
import com.auth0.android.Auth0
import com.example.devkots.uiLib.viewmodels.LoginViewModel
import com.example.devkots.uiLib.viewmodels.UserSessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test


class LoginViewModelAuth0IntegrationTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var userSessionViewModel: UserSessionViewModel
    private lateinit var auth0: Auth0

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        auth0 = Auth0.getInstance(
            "sxlOUkpm591uWTxtwpKguSg0rFneEFjJ",
            "dev-6vzyeoluir57rec7.us.auth0.com"
        )
        userSessionViewModel = UserSessionViewModel() // Mock or create a real instance
        viewModel = LoginViewModel(userSessionViewModel, auth0)
    }

    @Test
    fun testLoginWithValidCredentials() = runBlocking {
        val validEmail = "devkot@tec.mx"
        val validPassword = "DevKot*2025"

        // Simulate user input
        viewModel.onEmailChange(validEmail)
        viewModel.onPasswordChange(validPassword)

        // Attempt login

        withTimeout(5000) {viewModel.login()}

        // Wait for the result
        delay(2000)
        val isLoginSuccessful =  viewModel.loginSuccess.first()
        val errorMessage = viewModel.errorMessage.first()

        // Assertions
        assertTrue("Login should be successful with valid credentials", isLoginSuccessful)
        assertNull("Error message should be null on successful login", errorMessage)

    }
}
