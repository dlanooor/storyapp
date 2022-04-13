package com.example.storyapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.pojo.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var detailStories: ListStoryItem
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailStories = intent.getParcelableExtra<ListStoryItem>(STORIES) as ListStoryItem
        binding.tvItemName.text = detailStories.name
        binding.tvItemDescription.text = detailStories.description

        Glide.with(applicationContext)
            .load(detailStories.photoUrl)
            .into(binding.imItemPhoto)
    }

    companion object {
        const val STORIES = "stories"
    }
}