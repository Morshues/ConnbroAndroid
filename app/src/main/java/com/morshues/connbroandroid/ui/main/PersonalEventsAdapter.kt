package com.morshues.connbroandroid.ui.main

import android.graphics.Color
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.Event

class PersonalEventsAdapter :
    ListAdapter<Event, PersonalEventsAdapter.PersonalEventHolder>(DIFF_CALLBACK) {
    private var mOnItemClickListener: OnItemClickListener? = null

    fun getEventAt(position: Int): Event {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalEventHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_personal_event, parent, false)
        return PersonalEventHolder(itemView)
    }

    override fun onBindViewHolder(holder: PersonalEventHolder, position: Int) {
        val currentEvent = getItem(position)
        holder.apply {
            tvTitle.text = currentEvent.title
            currentEvent.startTime?.also {
                if (it.time < System.currentTimeMillis()) {
                    itemView.setBackgroundColor(Color.LTGRAY)
                } else {
                    itemView.setBackgroundColor(Color.WHITE)
                }
                tvTime.text = DateUtils.getRelativeTimeSpanString(it.time)
            } ?: run {
                tvTime.text = ""
            }
        }
    }

    inner class PersonalEventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        var tvTime: TextView = itemView.findViewById(R.id.tv_time)
        init {
            itemView.setOnClickListener {
                mOnItemClickListener?.onEventUpdate(getItem(adapterPosition))
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