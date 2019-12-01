package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.databinding.ItemPersonalInfoBinding
import com.morshues.connbroandroid.db.model.PersonalInfo

class PersonalInfoAdapter :
    ListAdapter<PersonalInfo, PersonalInfoAdapter.PersonalInfoHolder>(DIFF_CALLBACK) {
    private var mOnItemClickListener: OnItemClickListener? = null

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
                mOnItemClickListener?.onInfoUpdate(getItem(adapterPosition))
            }
        }

        fun bind(item: PersonalInfo) {
            binding.apply {
                info = item
                executePendingBindings()
            }
        }
    }

    interface OnItemClickListener {
        fun onInfoUpdate(info: PersonalInfo)
    }

    fun setOnItemClickListener(l: OnItemClickListener) {
        mOnItemClickListener = l
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