package com.example.storyapp.data.local.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.storyapp.DataDummy
import com.example.storyapp.MainCoroutineRule
import com.example.storyapp.data.remote.pojo.ListStoryItem
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.ui.adapter.ListStoriesAdapter
import com.example.storyapp.ui.viewmodel.PagedTestDataSources
import com.example.storyapp.ui.viewmodel.noopListUpdateCallback
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
class StoryRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `when Get Location Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val dummyLocation = DataDummy.generateDummyStoryEntity()
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRxdEJ0bDhXbFB4cnhnZDQiLCJpYXQiOjE2NDk3NTc5MjB9.pxZ4r33VUSpojfNL7KmsI3dqK2qk6Co1bxbkKmZj_To"
        Mockito.`when`(storyRepository.getLocation(token)).thenReturn(dummyLocation)
        val actualLocation = storyRepository.getLocation(token)
        Assert.assertNotNull(actualLocation)
        Assert.assertEquals(dummyLocation, actualLocation)
    }

    @Test
    fun `when Get Stories Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val dummyStory = DataDummy.generateDummyStoryEntity()
        val data = PagedTestDataSources.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRxdEJ0bDhXbFB4cnhnZDQiLCJpYXQiOjE2NDk3NTc5MjB9.pxZ4r33VUSpojfNL7KmsI3dqK2qk6Co1bxbkKmZj_To"
        Mockito.`when`(storyRepository.getStory(token)).thenReturn(story)
        val actualStory = storyRepository.getStory(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(actualStory)

        advanceUntilIdle()
        Mockito.verify(storyRepository).getStory(token)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
        Assert.assertEquals(dummyStory[0].photoUrl, differ.snapshot()[0]?.photoUrl)
        Assert.assertEquals(dummyStory[0].description, differ.snapshot()[0]?.description)
        Assert.assertEquals(dummyStory[0].createdAt, differ.snapshot()[0]?.createdAt)
        Assert.assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
        Assert.assertEquals(dummyStory[0].lat, differ.snapshot()[0]?.lat)
        Assert.assertEquals(dummyStory[0].lon, differ.snapshot()[0]?.lon)
    }

}