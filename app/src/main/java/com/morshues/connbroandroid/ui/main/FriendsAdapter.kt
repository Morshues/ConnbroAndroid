package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.databinding.ItemFriendBinding
import com.morshues.connbroandroid.db.model.PersonDetail

class FriendsAdapter : ListAdapter<PersonDetail, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val friend = getItem(position)
        (holder as FriendHolder).bind(friend)
    }

    class FriendHolder(
        private val binding: ItemFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.friend?.let { friend ->
                    navigateToFriend(friend, it)
                }
            }
        }

        private fun navigateToFriend(
            friend: PersonDetail,
            it: View
        ) {
            val direction =
                FriendListFragmentDirections.actionMainFragmentToFriendDetailFragment(
                    friend.person.id
                )
            it.findNavController().navigate(direction)
        }

        fun bind(item: PersonDetail) {
            binding.apply {
                friend = item
                executePendingBindings()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PersonDetail>() {
            override fun areItemsTheSame(oldItem: PersonDetail, newItem: PersonDetail): Boolean {
                return oldItem.person.id == newItem.person.id
            }

            override fun areContentsTheSame(oldItem: PersonDetail, newItem: PersonDetail): Boolean {
                return oldItem.person == newItem.person
            }
        }
    }
}