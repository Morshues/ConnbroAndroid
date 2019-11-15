package com.morshues.connbroandroid.ui.main

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.db.model.PersonalInfo
import com.morshues.connbroandroid.repo.ConnbroRepository
import com.morshues.connbroandroid.util.ContentEditUtils
import com.morshues.connbroandroid.util.DateUtils
import kotlinx.android.synthetic.main.fragment_friend_detail.*
import java.util.*

private const val ARG_FRIEND_ID = "argFriendId"

class FriendDetailFragment : Fragment() {
    private var friendId: Long? = null

    private lateinit var mRepository: ConnbroRepository

    private var mListener: OnFragmentInteractionListener? = null
    private var mRecyclerView: RecyclerView? = null
    private var detailAdapter: PersonalInfoAdapter? = null

    private lateinit var viewModel: FriendDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailAdapter = PersonalInfoAdapter()
        detailAdapter!!.setOnItemClickListener(object : PersonalInfoAdapter.OnItemClickListener{
            override fun onFirstNameClick(firstName: String) {
                val activity = activity?: return
                ContentEditUtils.editTextDialog(activity, firstName, R.string.first_name) {
                    viewModel.updateFirstName(it)
                }
            }
            override fun onMidNameClick(midName: String) {
                val activity = activity?: return
                ContentEditUtils.editTextDialog(activity, midName, R.string.mid_name) {
                    viewModel.updateMidName(it)
                }
            }
            override fun onLastNameClick(lastName: String) {
                val activity = activity?: return
                ContentEditUtils.editTextDialog(activity, lastName, R.string.last_name) {
                    viewModel.updateLastName(it)
                }
            }
            override fun onNickNameClick(nickName: String) {
                val activity = activity?: return
                ContentEditUtils.editTextDialog(activity, nickName, R.string.nick_name) {
                    viewModel.updateNickName(it)
                }
            }
            override fun onBirthdayClick(birthday: String) {
                val activity = activity?: return
                val c = DateUtils.toCalender(birthday)
                val dlg = DatePickerDialog(activity,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        viewModel.updateBirthday(DateUtils.toSqlDate(year, month, dayOfMonth))
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
                )
                dlg.show()
            }
            override fun onNoteClick(note: String) {
                val activity = activity?: return
                ContentEditUtils.editTextDialog(activity, note, R.string.note) {
                    viewModel.updateNote(it)
                }
            }
            override fun onInfoUpdate(info: PersonalInfo) {
                viewModel.updateInfo(info)
            }
        })

        mRecyclerView = rv_person_detail.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = detailAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            viewModelFactory { FriendDetailViewModel(mRepository, friendId!!) }
        ).get(FriendDetailViewModel::class.java)
        viewModel.friendData.observe(viewLifecycleOwner, Observer<PersonDetail> {
            detailAdapter?.setPersonalInfo(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.friend_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_info -> {
                val activity = activity?: return false
                ContentEditUtils.addPersonalInfoDialog(activity) { title, description ->
                    viewModel.insertInfo(title, description)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
