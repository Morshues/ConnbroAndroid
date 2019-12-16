package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.databinding.ItemPersonalEventBinding
import com.morshues.connbroandroid.db.model.Event

class PersonalEventsAdapter(private val userId: Long, private val friendId: Long) :
    ListAdapter<Event, PersonalEventsAdapter.PersonalEventHolder>(DIFF_CALLBACK) {

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
                val event = getItem(adapterPosition)
                val direction =
                    FriendDetailFragmentDirections.actionFriendDetailFragmentToEventEditingDialog(
                        userId,
                        friendId,
                        event.id
                    )
                binding.root.findNavController().navigate(direction)
            }
        }

        fun bind(item: Event) {
            binding.apply {
                event = item
                executePendingBindings()
            }
        }
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