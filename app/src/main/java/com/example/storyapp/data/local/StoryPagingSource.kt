package com.example.storyapp.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.remote.api.ApiService
import com.example.storyapp.data.remote.pojo.ListStoryItem

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        TODO("Not yet implemented")
    }
}