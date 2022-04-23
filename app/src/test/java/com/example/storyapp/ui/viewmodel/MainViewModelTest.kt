package com.example.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.DataDummy
import com.example.storyapp.MainCoroutineRule
import com.example.storyapp.data.remote.pojo.ListStoryItem
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.ui.adapter.ListStoriesAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var mainViewModel: MainViewModel

    @Test
    fun `when Get Story Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val dummyStory = DataDummy.generateDummyStoryEntity()
        val data = PagedTestDataSources.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<ListStoryItem>>()
        story.value = data
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTRxdEJ0bDhXbFB4cnhnZDQiLCJpYXQiOjE2NDk3NTc5MjB9.pxZ4r33VUSpojfNL7KmsI3dqK2qk6Co1bxbkKmZj_To"
        `when`(mainViewModel.getStories(token)).thenReturn(story)
        val actualStory = mainViewModel.getStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        differ.submitData(actualStory)

        advanceUntilIdle()
        Mockito.verify(mainViewModel).getStories(token)
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

    @Test
    fun `when Get Token Should Not Null`() = mainCoroutineRules.runBlockingTest {
        val expectedToken = MutableLiveData<String>()
        `when`(mainViewModel.userToken).thenReturn(expectedToken)
        val actualToken = mainViewModel.userToken
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

    // save token still need fixes
    @Test
    fun `when Save Token Is Sucess`() = mainCoroutineRules.runBlockingTest {
        val expectedToken = "token"
        mainViewModel.saveToken("token")
    }
}

class PagedTestDataSources private constructor(private val items: List<ListStoryItem>) :
    PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}