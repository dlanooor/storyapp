package com.example.storyapp.data.local.story

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.remote.api.ApiService
import com.example.storyapp.data.remote.pojo.ListStoryItem
import com.example.storyapp.util.wrapEspressoIdlingResource

class StoryRepository (private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return wrapEspressoIdlingResource {
            Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMediator(storyDatabase, apiService, "Bearer $token"),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getAllStory()
                }
            ).liveData
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun getLocation(token: String): List<ListStoryItem> {
        return wrapEspressoIdlingResource {
            apiService.getStoriesLocation("bearer $token").listStory as List<ListStoryItem>
        }
    }
}