package com.example.storyapp

import com.example.storyapp.data.remote.pojo.ListStoryItem

object DataDummy {
    fun generateDummyStoryEntity(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val story = ListStoryItem(
                "story-$i",
                "https://story-api.dicoding.dev/images/stories/photos-1650692384779_kGzIlM83.jpg",
                "2022-04-23T05:39:44.781Z",
                "User $i",
                "Desc $i",
                -6.4433013,
                106.8082497
            )
            storyList.add(story)
        }
        return storyList
    }
}