package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.local.UserSession

class AddStoryViewModel(pref: UserSession) : ViewModel() {
    var userToken : LiveData<String> = pref.getToken().asLiveData()
}