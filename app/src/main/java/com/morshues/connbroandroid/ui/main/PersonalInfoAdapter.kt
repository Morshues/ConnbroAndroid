package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
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
        holder.etTitle.text = currentInfo.title
        holder.etDescription.text = currentInfo.description
    }

    inner class PersonalInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var lytShow: ViewGroup = itemView.findViewById(R.id.lyt_show)
        private var lytEdit: ViewGroup = itemView.findViewById(R.id.lyt_edit)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        var etTitle: TextView = itemView.findViewById(R.id.et_title)
        var etDescription: TextView = itemView.findViewById(R.id.et_description)
        private var btnCancel = itemView.findViewById<Button>(R.id.btn_cancel)
        private var btnConfirm = itemView.findViewById<Button>(R.id.btn_confirm)
        init {
            lytShow.setOnLongClickListener {
                TransitionManager.beginDelayedTransition(itemView as CardView, AutoTransition())
                val info = getItem(adapterPosition)
                etTitle.text = info.title
                etDescription.text = info.description
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
                    val info = getItem(position)
                    info.title = etTitle.text.toString()
                    info.description = etDescription.text.toString()
                    mOnItemClickListener?.onInfoUpdate(info)
                }
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