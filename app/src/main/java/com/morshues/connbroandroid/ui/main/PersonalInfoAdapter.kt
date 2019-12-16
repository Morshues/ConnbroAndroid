package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.databinding.ItemPersonalInfoBinding
import com.morshues.connbroandroid.db.model.PersonalInfo

class PersonalInfoAdapter(private val userId: Long, private val friendId: Long) :
    ListAdapter<PersonalInfo, PersonalInfoAdapter.PersonalInfoHolder>(DIFF_CALLBACK) {

    fun getInfoAt(position: Int): PersonalInfo {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalInfoHolder {
        val binding =
            ItemPersonalInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonalInfoHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonalInfoHolder, position: Int) {
        val currentInfo = getItem(position)
        holder.bind(currentInfo)
    }

    inner class PersonalInfoHolder(
        private val binding: ItemPersonalInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                val info = getItem(adapterPosition)
                val direction =
                    FriendDetailFragmentDirections.actionFriendDetailFragmentToInfoEditingDialog(
                        userId,
                        friendId,
                        info.id
                    )
                binding.root.findNavController().navigate(direction)
            }
        }

        fun bind(item: PersonalInfo) {
            binding.apply {
                info = item
                executePendingBindings()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PersonalInfo>() {
            override fun areItemsTheSame(oldItem: PersonalInfo, newItem: PersonalInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PersonalInfo, newItem: PersonalInfo): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.description == newItem.description
            }
        }
    }
}