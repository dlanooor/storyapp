package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.local.UserSession
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserSession) : ViewModel() {
    fun saveToken(token: String) : String{
        viewModelScope.launch {
            pref.saveToken(token)
        }
        return token
    }
}