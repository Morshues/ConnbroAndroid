package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.db.model.PersonalInfo

class PersonalInfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var person: Person? = null
    private var info: List<PersonalInfo> = ArrayList()
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setPersonalInfo(personData: PersonDetail) {
        person = personData.person
        info = personData.info
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ITEM_TYPE_HEADER
        } else {
            ITEM_TYPE_CONTENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_HEADER) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.partial_person_detail, parent, false)
            HeaderViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_personal_info, parent, false)
            PersonalInfoHolder(itemView)
        }
    }

    override fun getItemCount(): Int {
        return info.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            person?.apply {
                holder.firstName.text = firstName
                holder.midName.text = midName
                holder.lastName.text = lastName
                holder.nickName.text = nickName
                holder.birthday.text = birthday.toString()
                holder.note.text = note
            }
        } else if (holder is PersonalInfoHolder){
            val currentInfo = info[position-1]
            holder.tvTitle.text = currentInfo.title
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var firstName: TextView = itemView.findViewById(R.id.tv_first_name)
        var midName: TextView = itemView.findViewById(R.id.tv_mid_name)
        var lastName: TextView = itemView.findViewById(R.id.tv_last_name)
        var nickName: TextView = itemView.findViewById(R.id.tv_nick_name)
        var birthday: TextView = itemView.findViewById(R.id.tv_birthday)
        var note: TextView = itemView.findViewById(R.id.tv_note)
        init {
            firstName.setOnClickListener {
                mOnItemClickListener?.onFirstNameClick(firstName.text.toString())
            }
            midName.setOnClickListener {
                mOnItemClickListener?.onMidNameClick(midName.text.toString())
            }
            lastName.setOnClickListener {
                mOnItemClickListener?.onLastNameClick(lastName.text.toString())
            }
            nickName.setOnClickListener {
                mOnItemClickListener?.onNickNameClick(nickName.text.toString())
            }
            birthday.setOnClickListener {
                mOnItemClickListener?.onBirthdayClick(birthday.text.toString())
            }
            note.setOnClickListener {
                mOnItemClickListener?.onNoteClick(note.text.toString())
            }
        }
    }

    inner class PersonalInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mOnItemClickListener?.onItemClick(info[position-1])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(person: PersonalInfo)
        fun onFirstNameClick(firstName: String)
        fun onMidNameClick(midName: String)
        fun onLastNameClick(lastName: String)
        fun onNickNameClick(nickName: String)
        fun onBirthdayClick(birthday: String)
        fun onNoteClick(note: String)
    }

    fun setOnItemClickListener(l: OnItemClickListener) {
        mOnItemClickListener = l
    }

    companion object {
        private const val ITEM_TYPE_HEADER = 0
        private const val ITEM_TYPE_CONTENT = 1
    }
}