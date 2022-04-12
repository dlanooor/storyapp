package com.example.storyapp.ui.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.remote.pojo.ListStoryItem
import com.example.storyapp.data.remote.pojo.LoginResult
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.adapter.ListStoriesAdapter
import com.example.storyapp.ui.viewmodel.MainViewModel
import com.example.storyapp.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userModel: LoginResult
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userModel = intent.getParcelableExtra<LoginResult>(USER_SESSION) as LoginResult

        mainViewModel = ViewModelProvider(this, ViewModelFactory()).get(
            MainViewModel::class.java
        )
        mainViewModel.getAllStories(userModel.token as String)
        mainViewModel.listStories.observe(this) {listStories ->
            showRecyclerList(listStories)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                val i = Intent(this, AddStoryActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.logout -> {
//                val i = Intent(this, MenuActivity::class.java)
//                startActivity(i)
                return true
            }
            else -> return true
        }
    }

    private fun showRecyclerList(listStories: List<ListStoryItem>) {
        binding.apply {
            rvStories.setHasFixedSize(true)

            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                rvStories.layoutManager = GridLayoutManager(this@MainActivity, 4)
            else
                rvStories.layoutManager = GridLayoutManager(this@MainActivity, 2)

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