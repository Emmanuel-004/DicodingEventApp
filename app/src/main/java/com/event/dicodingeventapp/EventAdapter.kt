package com.event.dicodingeventapp

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.event.dicodingeventapp.data.local.entity.EventEntity
import com.event.dicodingeventapp.databinding.ItemEventBinding


class EventAdapter(private val onBookmarkClick: (EventEntity) -> Unit) : ListAdapter<EventEntity, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id.toString())
            holder.itemView.context.startActivity(intent)
        }
        val ivFavorite = holder.binding.ivFavorite
        if (event.isBookmarked) {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.baseline_favorite_24))
        } else {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.baseline_favorite_border_24))
        }
        ivFavorite.setOnClickListener {
            onBookmarkClick(event)
        }
    }

    inner class MyViewHolder(internal val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            Log.d("EventAdapter", "link value: ${event.mediaCover}, event: ${event}")
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.ivEventImage)
            binding.tvEventName.text = event.name
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<EventEntity> =
        object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}