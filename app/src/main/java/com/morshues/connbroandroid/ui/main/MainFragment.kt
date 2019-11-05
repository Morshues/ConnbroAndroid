package com.morshues.connbroandroid.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.PersonDetail
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    private var mRecyclerView: RecyclerView? = null
    private var friendsAdapter: FriendsAdapter? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var friendsViewModel: FriendsViewModel

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
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        rootView.btn_add.setOnClickListener {
            mListener?.onFragmentChange(NewFriendFragment::class.java)
        }

        friendsAdapter = FriendsAdapter()

        mRecyclerView = rootView.rv_friends.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = friendsAdapter
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel::class.java)
        friendsViewModel.allFriends.observe(this, Observer<List<PersonDetail>> {
            friendsAdapter?.setFriends(it)
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}