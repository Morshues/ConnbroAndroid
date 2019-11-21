package com.morshues.connbroandroid.ui.main

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.db.model.PersonalInfo
import com.morshues.connbroandroid.repo.ConnbroRepository
import com.morshues.connbroandroid.util.ContentEditUtils
import com.morshues.connbroandroid.util.DateTimeUtils
import kotlinx.android.synthetic.main.fragment_friend_detail.*
import kotlinx.android.synthetic.main.partial_event_editing.view.*
import kotlinx.android.synthetic.main.partial_person_detail.*
import kotlinx.android.synthetic.main.partial_personal_info_editing.view.btn_cancel
import kotlinx.android.synthetic.main.partial_personal_info_editing.view.btn_confirm
import kotlinx.android.synthetic.main.partial_personal_info_editing.view.et_description
import kotlinx.android.synthetic.main.partial_personal_info_editing.view.et_title
import java.sql.Date
import java.util.*

private const val ARG_FRIEND_ID = "argFriendId"

class FriendDetailFragment : Fragment() {
    private var friendId: Long? = null

    private lateinit var mRepository: ConnbroRepository

    private var mListener: OnFragmentInteractionListener? = null
    private var eventRecyclerView: RecyclerView? = null
    private var eventAdapter: PersonalEventsAdapter? = null
    private var infoRecyclerView: RecyclerView? = null
    private var infoAdapter: PersonalInfoAdapter? = null

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
        tv_first_name.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity?: return@setOnClickListener
            ContentEditUtils.editTextDialog(activity, textView.text.toString(), R.string.first_name) {
                viewModel.updateFirstName(it)
            }
        }
        tv_mid_name.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity?: return@setOnClickListener
            ContentEditUtils.editTextDialog(activity, textView.text.toString(), R.string.mid_name) {
                viewModel.updateMidName(it)
            }
        }
        tv_last_name.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity?: return@setOnClickListener
            ContentEditUtils.editTextDialog(activity, textView.text.toString(), R.string.last_name) {
                viewModel.updateLastName(it)
            }
        }
        tv_nick_name.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity?: return@setOnClickListener
            ContentEditUtils.editTextDialog(activity, textView.text.toString(), R.string.nick_name) {
                viewModel.updateNickName(it)
            }
        }
        tv_birthday.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity?: return@setOnClickListener
            val c = DateTimeUtils.dateToCalender(textView.text)
            val dlg = DatePickerDialog(activity,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.updateBirthday(DateTimeUtils.toSqlDate(year, month, dayOfMonth))
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
            dlg.show()
        }
        tv_note.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity?: return@setOnClickListener
            ContentEditUtils.editTextDialog(activity, textView.text.toString(), R.string.note, false) {
                viewModel.updateNote(it)
            }
        }

        lyt_create_event.apply {
            btn_cancel.setOnClickListener {
                et_title.setText("")
                et_description.setText("")
                visibility = View.GONE
            }
            btn_confirm.setOnClickListener {
                if (et_title.text.toString().isBlank()) {
                    Snackbar.make(it, R.string.msg_title_cannot_be_null, Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.insertEvent(
                    title = et_title.text.toString(),
                    description = et_description.text.toString(),
                    startTime = v_start_at.getDateTime(),
                    endTime = Date(System.currentTimeMillis())
                )
                et_title.setText("")
                et_description.setText("")
                v_start_at.reset()
                visibility = View.GONE
            }
        }

        eventAdapter = PersonalEventsAdapter()
        eventAdapter!!.setOnItemClickListener(object : PersonalEventsAdapter.OnItemClickListener{
            override fun onEventUpdate(event: Event) {
                viewModel.updateEvent(event)
            }
        })

        eventRecyclerView = rv_events.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = eventAdapter
        }

        lyt_create_info.apply {
            btn_cancel.setOnClickListener {
                et_title.setText("")
                et_description.setText("")
                visibility = View.GONE
            }
            btn_confirm.setOnClickListener {
                if (et_title.text.toString().isBlank()) {
                    Snackbar.make(it, R.string.msg_title_cannot_be_null, Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.insertInfo(et_title.text.toString(), et_description.text.toString())
                et_title.setText("")
                et_description.setText("")
                visibility = View.GONE
            }
        }

        infoAdapter = PersonalInfoAdapter()
        infoAdapter!!.setOnItemClickListener(object : PersonalInfoAdapter.OnItemClickListener{
            override fun onInfoUpdate(info: PersonalInfo) {
                viewModel.updateInfo(info)
            }
        })

        infoRecyclerView = rv_person_info.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = infoAdapter
        }
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
            tv_note.text = person.note
            eventAdapter?.submitList(it.events)
            infoAdapter?.submitList(it.sortedInfo())
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteInfo(infoAdapter!!.getInfoAt(position))
                Snackbar.make(viewHolder.itemView, "Deleted", Snackbar.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(infoRecyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.friend_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_info -> {
                lyt_create_info.visibility = View.VISIBLE
                true
            }
            R.id.action_add_event -> {
                lyt_create_event.visibility = View.VISIBLE
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
