package com.example.devkots.uiLib.viewmodels

import com.auth0.android.Auth0
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import io.mockk.mockk

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val userSessionViewModel: UserSessionViewModel = mockk(relaxed = true)
    private val auth0: Auth0 = mockk()

    @Test
    fun loginValuesUpdateTest() = runTest {
        val viewModel = LoginViewModel(userSessionViewModel, auth0)

        var newEmail = "test@example.com"
        var newPassword = "password"
        viewModel.onEmailChange(newEmail)
        viewModel.onPasswordChange(newPassword)
        assertEquals(newEmail, viewModel.email.first())
        assertEquals(newPassword, viewModel.password.first())

        newEmail = "test2"
        newPassword = "password2"

        assertNotEquals(newEmail, viewModel.email.first())
        assertNotEquals(newPassword, viewModel.password.first())
    }
}
