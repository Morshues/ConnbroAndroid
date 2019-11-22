package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.PersonalInfo

class PersonalInfoAdapter :
    ListAdapter<PersonalInfo, PersonalInfoAdapter.PersonalInfoHolder>(DIFF_CALLBACK) {
    private var mOnItemClickListener: OnItemClickListener? = null

    fun getInfoAt(position: Int): PersonalInfo {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalInfoHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_personal_info, parent, false)
        return PersonalInfoHolder(itemView)
    }

    override fun onBindViewHolder(holder: PersonalInfoHolder, position: Int) {
        val currentInfo = getItem(position)
        holder.tvTitle.text = currentInfo.title
    }

    inner class PersonalInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        init {
            itemView.setOnClickListener {
                mOnItemClickListener?.onInfoUpdate(getItem(adapterPosition))
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