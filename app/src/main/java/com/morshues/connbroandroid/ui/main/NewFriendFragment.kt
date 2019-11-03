package com.morshues.connbroandroid.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.Person
import kotlinx.android.synthetic.main.fragment_new_friend.view.*
import java.sql.Date

class NewFriendFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    companion object {
        fun newInstance() = NewFriendFragment()
    }

    private lateinit var viewModel: NewFriendViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentChangeListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_new_friend, container, false)

        rootView.btn_create_friend.setOnClickListener {
            rootView.apply {
                val newFriend = Person(
                    firstName = et_first_name.text.toString(),
                    midName = et_mid_name.text.toString(),
                    lastName = et_last_name.text.toString(),
                    nickName = et_nick_name.text.toString(),
                    birthday = Date(dp_birth.year, dp_birth.month, dp_birth.dayOfMonth)
                )
                viewModel.insert(newFriend)
                mListener?.onFragmentChange(MainFragment::class.java)
            }

        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewFriendViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}
