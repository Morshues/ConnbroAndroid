package com.morshues.connbroandroid.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.PersonDetail

class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.FriendHolder>() {
    private var friends: List<PersonDetail>  = ArrayList()
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendHolder(itemView)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        val currentFriend = friends[position]
        holder.tvName.text = currentFriend.person.showingName()
    }

    fun setFriends(friends: List<PersonDetail>) {
        this.friends = friends
        notifyDataSetChanged()
    }

    inner class FriendHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mOnItemClickListener?.onItemClick(friends[position])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(person: PersonDetail)
    }

    fun setOnItemClickListener(l: OnItemClickListener) {
        mOnItemClickListener = l
    }
}