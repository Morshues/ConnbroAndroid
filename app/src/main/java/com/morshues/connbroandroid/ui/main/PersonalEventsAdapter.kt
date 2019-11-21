package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.util.DateTimeUtils
import com.morshues.connbroandroid.widget.DateSpinner
import com.morshues.connbroandroid.widget.TimeSpinner

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
        holder.tvTitle.text = currentEvent.title
        holder.etTitle.text = currentEvent.title
        holder.etDescription.text = currentEvent.description
        holder.spnStartDate.setDate(currentEvent.startTime)
        holder.spnStartTime.setTime(currentEvent.startTime)
    }

    inner class PersonalEventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var lytShow: ViewGroup = itemView.findViewById(R.id.lyt_show)
        private var lytEdit: ViewGroup = itemView.findViewById(R.id.lyt_edit)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        var etTitle: TextView = itemView.findViewById(R.id.et_title)
        var etDescription: TextView = itemView.findViewById(R.id.et_description)
        var spnStartDate: DateSpinner = itemView.findViewById(R.id.spn_start_date)
        var spnStartTime: TimeSpinner = itemView.findViewById(R.id.spn_start_time)
        private var btnCancel = itemView.findViewById<Button>(R.id.btn_cancel)
        private var btnConfirm = itemView.findViewById<Button>(R.id.btn_confirm)
        init {
            lytShow.setOnLongClickListener {
                TransitionManager.beginDelayedTransition(itemView as CardView, AutoTransition())
                val event = getItem(adapterPosition)
                etTitle.text = event.title
                etDescription.text = event.description
                spnStartDate.setDate(event.startTime)
                spnStartTime.setTime(event.startTime)
                lytShow.visibility = View.GONE
                lytEdit.visibility = View.VISIBLE
                true
            }
            btnCancel.setOnClickListener {
                lytShow.visibility = View.VISIBLE
                lytEdit.visibility = View.GONE
            }
            btnConfirm.setOnClickListener {
                if (etTitle.text.toString().isBlank()) {
                    Snackbar.make(it, R.string.msg_title_cannot_be_null, Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                lytShow.visibility = View.VISIBLE
                lytEdit.visibility = View.GONE
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = getItem(position)
                    event.title = etTitle.text.toString()
                    event.description = etDescription.text.toString()
                    event.startTime = DateTimeUtils.combineDateTime(
                        spnStartDate.getDate(),
                        spnStartTime.getTime()
                    )
                    mOnItemClickListener?.onEventUpdate(event)
                }
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
                        oldItem.description == newItem.description
            }
        }
    }
}