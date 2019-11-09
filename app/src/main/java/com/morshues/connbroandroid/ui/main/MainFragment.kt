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
import com.morshues.connbroandroid.Page
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.repo.ConnbroRepository
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var mRepository: ConnbroRepository

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
            mRepository = context.getRepository()
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add.setOnClickListener {
            mListener?.onFragmentChange(Page.FriendCreatePage)
        }

        friendsAdapter = FriendsAdapter()
        friendsAdapter!!.setOnItemClickListener(object : FriendsAdapter.OnItemClickListener{
            override fun onItemClick(person: PersonDetail) {
                mListener?.onFragmentChange(Page.FriendDetailPage(person.person.id))
            }
        })

        mRecyclerView = rv_friends.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = friendsAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        friendsViewModel = ViewModelProviders.of(
            this,
            viewModelFactory { FriendsViewModel(mRepository) }
        ).get(FriendsViewModel::class.java)
        friendsViewModel.allFriends.observe(viewLifecycleOwner, Observer<List<PersonDetail>> {
            friendsAdapter?.setFriends(it)
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}
