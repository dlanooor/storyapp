package com.example.storyapp.data.local.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.local.StoryPagingSource
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.remote.api.ApiService
import com.example.storyapp.data.remote.pojo.ListStoryItem

class StoryRepository (private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    suspend fun getLocation(token: String): List<ListStoryItem> {
        return apiService.getStoriesLocation("bearer $token").listStory as List<ListStoryItem>
    }
}