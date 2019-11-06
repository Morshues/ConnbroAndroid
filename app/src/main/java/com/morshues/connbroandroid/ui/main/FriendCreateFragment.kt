package com.morshues.connbroandroid.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.morshues.connbroandroid.Page

import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.repo.ConnbroRepository
import kotlinx.android.synthetic.main.fragment_friend_create.view.*
import java.sql.Date

class FriendCreateFragment(
    private val mRepository: ConnbroRepository
) : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    companion object {
        fun newInstance(repository: ConnbroRepository) = FriendCreateFragment(repository)
    }

    private lateinit var rootView: View
    private lateinit var viewModel: FriendCreateViewModel

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
        rootView = inflater.inflate(R.layout.fragment_friend_create, container, false)

        rootView.btn_create_friend.setOnClickListener {
            createFriend()
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            viewModelFactory { FriendCreateViewModel(mRepository) }
        ).get(FriendCreateViewModel::class.java)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun createFriend() {
        rootView.apply {
            val newFriend = Person(
                firstName = et_first_name.text.toString().trim(),
                midName = et_mid_name.text.toString().trim(),
                lastName = et_last_name.text.toString().trim(),
                nickName = et_nick_name.text.toString().trim(),
                birthday = Date(dp_birth.year, dp_birth.month, dp_birth.dayOfMonth),
                description = et_description.text.toString().trim()
            )
            if (newFriend.nickName.isNotBlank() || newFriend.fullName().isNotBlank()) {
                viewModel.insert(newFriend)
                mListener?.onFragmentChange(Page.MAIN)
            } else {
                Snackbar.make(
                    rootView,
                    "Name or NickName can't not be blank",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

}