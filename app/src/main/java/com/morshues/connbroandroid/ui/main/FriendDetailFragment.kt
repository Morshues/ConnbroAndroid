package com.morshues.connbroandroid.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.repo.ConnbroRepository
import kotlinx.android.synthetic.main.fragment_friend_detail.*

private const val ARG_FRIEND_ID = "argFriendId"

class FriendDetailFragment : Fragment() {
    private var friendId: Long? = null

    private lateinit var mRepository: ConnbroRepository

    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: FriendDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            friendId = it.getLong(ARG_FRIEND_ID)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
            mRepository = context.getRepository()
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friend_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            viewModelFactory { FriendDetailViewModel(mRepository, friendId!!) }
        ).get(FriendDetailViewModel::class.java)
        viewModel.friendData.observe(viewLifecycleOwner, Observer<PersonDetail> {
            val person = it.person
            tv_first_name.text = person.firstName
            tv_mid_name.text = person.midName
            tv_last_name.text = person.lastName
            tv_nick_name.text = person.nickName
            tv_birthday.text = person.birthday.toString()
            tv_description.text = person.description
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(friendId: Long) =
            FriendDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_FRIEND_ID, friendId)
                }
            }
    }
}
