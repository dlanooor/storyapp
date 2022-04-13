package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.local.UserSession

class AddStoryViewModel(private val pref: UserSession) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }



    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}