package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.databinding.ItemPersonalEventBinding
import com.morshues.connbroandroid.db.model.Event

class PersonalEventsAdapter :
    ListAdapter<Event, PersonalEventsAdapter.PersonalEventHolder>(DIFF_CALLBACK) {
    private var mOnItemClickListener: OnItemClickListener? = null

    fun getEventAt(position: Int): Event {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalEventHolder {
        val binding =
            ItemPersonalEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonalEventHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonalEventHolder, position: Int) {
        val currentEvent = getItem(position)
        holder.bind(currentEvent)
    }

    inner class PersonalEventHolder(
        private val binding: ItemPersonalEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                mOnItemClickListener?.onEventUpdate(getItem(adapterPosition))
            }
        }

        fun bind(item: Event) {
            binding.apply {
                event = item
                executePendingBindings()
            }
        }
    }

    interface OnItemClickListener {
        fun onEventUpdate(event: Event)
    }

    fun setOnItemClickListener(l: OnItemClickListener) {
        mOnItemClickListener = l
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.description == newItem.description &&
                        oldItem.startTime == newItem.startTime &&
                        oldItem.endTime == newItem.endTime
            }
        }
    }
}