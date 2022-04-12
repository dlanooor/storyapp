package com.example.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.pojo.ListStoryItem

class ListStoriesAdapter(private val listStories: ArrayList<ListStoryItem>) :
    RecyclerView.Adapter<ListStoriesAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_stories, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.tvUsername.text = listStories[position].name
        Glide.with(holder.itemView.context).load(listStories[position].photoUrl).into(holder.imgPhoto)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listStories[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listStories.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.im_item_photo)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_item_name)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

}