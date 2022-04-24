package com.example.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.DataDummy
import com.example.storyapp.MainCoroutineRule
import com.example.storyapp.data.remote.pojo.ListStoryItem
import com.example.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var mapsViewModel: MapsViewModel

    @Test
    fun `when Get Token Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val expectedToken = MutableLiveData<String>()
        Mockito.`when`(mapsViewModel.userToken).thenReturn(expectedToken)
        val actualToken = mapsViewModel.userToken
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

    @Test
    fun `when Get Location Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val dummyStory = DataDummy.generateDummyStoryEntity()
        val story = MutableLiveData<List<ListStoryItem>>()
        story.value = dummyStory
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRxdEJ0bDhXbFB4cnhnZDQiLCJpYXQiOjE2NDk3NTc5MjB9.pxZ4r33VUSpojfNL7KmsI3dqK2qk6Co1bxbkKmZj_To"
        mapsViewModel.getLocation(token)
        Mockito.`when`(mapsViewModel.listStory).thenReturn(story)
        val actualStory = mapsViewModel.listStory.getOrAwaitValue()
        Assert.assertNotNull(actualStory)
        Assert.assertEquals(dummyStory.size, actualStory.size)
        Assert.assertEquals(dummyStory, actualStory)
    }
}