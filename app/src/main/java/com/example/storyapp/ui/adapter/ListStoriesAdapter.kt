package com.example.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.pojo.ListStoryItem
import com.example.storyapp.ui.activity.DetailActivity
import androidx.core.util.Pair

class ListStoriesAdapter(private val listStories: ArrayList<ListStoryItem>) :
    RecyclerView.Adapter<ListStoriesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_stories, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStories[position])
    }

    override fun getItemCount(): Int = listStories.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.im_item_photo)
        private var tvUsername: TextView = itemView.findViewById(R.id.tv_item_name)
        private var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)

        fun bind(stories: ListStoryItem) {
            tvUsername.text = stories.name
            tvDescription.text = stories.description
            Glide.with(itemView.context).load(stories.photoUrl).into(imgPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(STORIES, stories)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "profile"),
                        Pair(tvUsername, "name"),
                        Pair(tvDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        const val STORIES = "stories"
    }

}