package com.example.storyapp.data.local.di

import android.content.Context
import com.example.storyapp.data.local.story.StoryRepository
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.remote.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}