package com.example.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var loginViewModel: LoginViewModel

    @Test
    fun `when Save Token Is Success and Not Null`() = mainCoroutineRules.runBlockingTest {
        val expectedToken = "token"
        Mockito.`when`(loginViewModel.saveToken("token")).thenReturn(expectedToken)
        val actualToken = loginViewModel.saveToken("token")
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(expectedToken, actualToken)
    }
}