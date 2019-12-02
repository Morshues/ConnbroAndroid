package com.morshues.connbroandroid.ui.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.databinding.ItemEventBinding
import com.morshues.connbroandroid.db.model.Event

class EventAdapter : ListAdapter<Event, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)
        (holder as EventHolder).bind(event)
    }

    class EventHolder(
        private val binding: ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.event?.let { friend ->
                    navigateToEvent(friend, it)
                }
            }
        }

        private fun navigateToEvent(
            event: Event,
            it: View
        ) {
            val direction =
                EventListFragmentDirections.actionEventListFragmentToEventDetailFragment(
                    event.id
                )
            it.findNavController().navigate(direction)
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
                return oldItem.title == newItem.title && oldItem.description == newItem.description
            }
        }
    }
}