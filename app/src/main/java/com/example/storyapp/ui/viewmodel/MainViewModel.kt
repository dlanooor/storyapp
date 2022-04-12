package com.example.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.api.ApiConfig
import com.example.storyapp.data.remote.pojo.GetAllStories
import com.example.storyapp.data.remote.pojo.ListStoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel() : ViewModel() {
    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStories(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllStories("Bearer " + token)
        client.enqueue(object : Callback<GetAllStories> {
            override fun onResponse(call: Call<GetAllStories>, response: Response<GetAllStories>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStories.value = response.body()?.listStory as List<ListStoryItem>?
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetAllStories>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}