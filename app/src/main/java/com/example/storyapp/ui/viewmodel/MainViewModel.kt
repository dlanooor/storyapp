package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.storyapp.data.local.UserSession
import com.example.storyapp.data.local.data.StoryRepository
import com.example.storyapp.data.remote.pojo.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserSession, private val storyRepository: StoryRepository) : ViewModel() {
    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun getAllStories(token: String){
        _isLoading.value = true
        viewModelScope.launch {
            _listStories.postValue(storyRepository.getStory(token))
        }
        _isLoading.value = false
    }

}