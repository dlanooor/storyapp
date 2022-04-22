package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.storyapp.data.local.UserSession
import com.example.storyapp.data.local.data.StoryRepository
import com.example.storyapp.data.remote.pojo.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel (private val pref: UserSession, private val storyRepository: StoryRepository) : ViewModel() {
    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    var listStory : LiveData<List<ListStoryItem>> = _listStory

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun getLocation(token: String) {
        viewModelScope.launch {
            _listStory.postValue(storyRepository.getLocation(token))
        }
    }

}