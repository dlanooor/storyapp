package com.example.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
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
class AddStoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var addStoryViewModel: AddStoryViewModel

    @Test
    fun `when Get Token Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val expectedToken = MutableLiveData<String>()
        Mockito.`when`(addStoryViewModel.userToken).thenReturn(expectedToken)
        val actualToken = addStoryViewModel.userToken
        Assert.assertNotNull(actualToken)
        Assert.assertEquals(expectedToken, actualToken)
    }

    @Test
    fun `when Get Token Is Null`() = mainCoroutineRules.runBlockingTest {
        val expectedToken = MutableLiveData<String>()
        val actualToken = null
        Assert.assertNull(actualToken)
        Assert.assertNotEquals(expectedToken, actualToken)
    }
}