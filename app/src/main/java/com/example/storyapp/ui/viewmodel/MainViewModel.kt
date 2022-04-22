package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.local.UserSession
import com.example.storyapp.data.local.data.StoryRepository
import com.example.storyapp.data.remote.pojo.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserSession, private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> = storyRepository.getStory(token).cachedIn(viewModelScope)
}