package com.example.mediauploader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MediaAdapter(
    private val mediaList: List<String>,
    private val serverUrl: String,
    private val selectable: Boolean = false
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private val selectedItems = mutableSetOf<String>()

    inner class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.mediaThumbnail)
        val checkbox: CheckBox = view.findViewById(R.id.mediaCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val url = "$serverUrl/media/${mediaList[position]}"
        val fileName = mediaList[position]

        Glide.with(holder.thumbnail.context)
            .load(url)
            .placeholder(R.drawable.ic_video_placeholder)
            .into(holder.thumbnail)

        if (selectable) {
            holder.checkbox.visibility = View.VISIBLE
            holder.checkbox.isChecked = selectedItems.contains(fileName)

            holder.itemView.setOnClickListener {
                holder.checkbox.isChecked = !holder.checkbox.isChecked
            }

            holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedItems.add(fileName)
                else selectedItems.remove(fileName)
            }
        } else {
            holder.checkbox.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = mediaList.size

    fun getSelectedMedia(): List<String> = selectedItems.toList()
}
