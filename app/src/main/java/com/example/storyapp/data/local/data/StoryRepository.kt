package com.example.storyapp.data.local.data

import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.remote.api.ApiService
import com.example.storyapp.data.remote.pojo.ListStoryItem

class StoryRepository (private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    suspend fun getStory(token: String): List<ListStoryItem> {
        return apiService.getAllStories("Bearer $token", 1, 10, 1).listStory as List<ListStoryItem>
    }
}