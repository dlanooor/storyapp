package com.example.storyapp.ui.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.local.UserModel
import com.example.storyapp.data.remote.pojo.ListStoryItem
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.adapter.ListStoriesAdapter
import com.example.storyapp.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userModel: UserModel
    private var mainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userModel = intent.getParcelableExtra<UserModel>(USER_SESSION) as UserModel

        mainViewModel.getAllStories(userModel.token as String)
        mainViewModel.listStories.observe(this) {listStories ->
            showRecyclerList(listStories)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showRecyclerList(listStories: List<ListStoryItem>) {
        binding.apply {
            rvStories.setHasFixedSize(true)

            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                rvStories.layoutManager = GridLayoutManager(this@MainActivity, 4)
            else
                rvStories.layoutManager = GridLayoutManager(this@MainActivity, 3)

            val arrayListStories = ArrayList<ListStoryItem>()
            arrayListStories.addAll(listStories)
            val listStoriesAdapter = ListStoriesAdapter(arrayListStories)
            rvStories.adapter = listStoriesAdapter

            listStoriesAdapter.setOnItemClickCallback(object : ListStoriesAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListStoryItem) {
                    val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
                    intentToDetail.putExtra(STORIES, data)
                    startActivity(intentToDetail)
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    companion object {
        const val USER_SESSION = "user_session"
        const val STORIES = "stories"
    }

}