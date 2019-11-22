package com.morshues.connbroandroid.ui.main

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.partial_personal_info_editing.view.et_description
import kotlinx.android.synthetic.main.partial_personal_info_editing.view.et_title
import java.sql.Date
import java.util.*

class FriendDetailFragment : Fragment() {
    private var friendId: Long = 0

    private lateinit var mRepository: ConnbroRepository

    private var mListener: OnFragmentInteractionListener? = null
    private var eventRecyclerView: RecyclerView? = null
    private var eventAdapter: PersonalEventsAdapter? = null
    private var infoRecyclerView: RecyclerView? = null
    private var infoAdapter: PersonalInfoAdapter? = null

    private lateinit var rootView: View
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
        rootView = inflater.inflate(R.layout.fragment_friend_detail, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_first_name.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity ?: return@setOnClickListener
            ContentEditUtils.editTextDialog(
                activity,
                textView.text.toString(),
                R.string.first_name
            ) {
                viewModel.updateFirstName(it)
            }
        }
        tv_mid_name.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity ?: return@setOnClickListener
            ContentEditUtils.editTextDialog(activity, textView.text.toString(), R.string.mid_name) {
                viewModel.updateMidName(it)
            }
        }
        tv_last_name.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity ?: return@setOnClickListener
            ContentEditUtils.editTextDialog(
                activity,
                textView.text.toString(),
                R.string.last_name
            ) {
                viewModel.updateLastName(it)
            }
        }
        tv_nick_name.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity ?: return@setOnClickListener
            ContentEditUtils.editTextDialog(
                activity,
                textView.text.toString(),
                R.string.nick_name
            ) {
                viewModel.updateNickName(it)
            }
        }
        tv_birthday.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity ?: return@setOnClickListener
            val c = DateTimeUtils.dateToCalender(textView.text)
            val dlg = DatePickerDialog(
                activity,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.updateBirthday(DateTimeUtils.toSqlDate(year, month, dayOfMonth))
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
            )
            dlg.show()
        }
        tv_note.setOnClickListener { textView ->
            if (textView !is TextView) return@setOnClickListener
            val activity = activity ?: return@setOnClickListener
            ContentEditUtils.editTextDialog(
                activity,
                textView.text.toString(),
                R.string.note,
                false
            ) {
                viewModel.updateNote(it)
            }
        }

        eventAdapter = PersonalEventsAdapter()
        eventAdapter!!.setOnItemClickListener(object : PersonalEventsAdapter.OnItemClickListener {
            override fun onEventUpdate(event: Event) {
                editEventDialog(event) {
                    viewModel.updateEvent(it)
                }
            }
        })

        eventRecyclerView = rv_events.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = eventAdapter
        }

        infoAdapter = PersonalInfoAdapter()
        infoAdapter!!.setOnItemClickListener(object : PersonalInfoAdapter.OnItemClickListener {
            override fun onInfoUpdate(info: PersonalInfo) {
                editInfoDialog(info) {
                    viewModel.updateInfo(it)
                }
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
            viewModelFactory { FriendDetailViewModel(mRepository, friendId) }
        ).get(FriendDetailViewModel::class.java)
        viewModel.friendData.observe(viewLifecycleOwner, Observer<PersonDetail> {
            val person = it.person
            tv_first_name.text = person.firstName
            tv_mid_name.text = person.midName
            tv_last_name.text = person.lastName
            tv_nick_name.text = person.nickName
            tv_birthday.text = person.birthday.toString()
            tv_note.text = person.note
            eventAdapter?.submitList(it.sortedEvents())
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
                viewModel.deleteEvent(eventAdapter!!.getEventAt(position))
                Snackbar.make(viewHolder.itemView, R.string.deleted, Snackbar.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(eventRecyclerView)

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
                Snackbar.make(viewHolder.itemView, R.string.deleted, Snackbar.LENGTH_SHORT).show()
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
                editInfoDialog(null) {
                    viewModel.insertInfo(it)
                }
                true
            }
            R.id.action_add_event -> {
                editEventDialog(null) {
                    viewModel.insertEvent(it)
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

    private fun editEventDialog(event: Event?, callback: (event: Event) -> Unit) {
        val context = activity ?: return
        val eventId = event?.id ?: 0
        val resStrConfirm = if (event == null) R.string.add else R.string.update
        val builder = AlertDialog.Builder(context).apply {
            val input = View.inflate(context, R.layout.partial_event_editing, null)
            if (event != null) {
                with(input) {
                    et_title.setText(event.title)
                    et_description.setText(event.description)
                    v_start_at.setDateTime(event.startTime)
                }
            }
            setTitle(R.string.personal_event)
            setView(input)
            setPositiveButton(resStrConfirm) { dialog, _ ->
                if (input.et_title.text.toString().isBlank()) {
                    Snackbar.make(
                        rootView,
                        R.string.msg_title_cannot_be_blank,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                dialog.dismiss()
                val newEvent = Event(
                    id = eventId,
                    title = input.et_title.text.toString(),
                    description = input.et_description.text.toString(),
                    startTime = input.v_start_at.getDateTime(),
                    endTime = Date(System.currentTimeMillis())
                )
                callback(newEvent)
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder.create().show()
    }

    private fun editInfoDialog(info: PersonalInfo?, callback: (info: PersonalInfo) -> Unit) {
        val context = activity ?: return
        val infoId = info?.id ?: 0
        val resStrConfirm = if (info == null) R.string.add else R.string.update
        val builder = AlertDialog.Builder(context).apply {
            val input = View.inflate(context, R.layout.partial_personal_info_editing, null)
            if (info != null) {
                with(input) {
                    et_title.setText(info.title)
                    et_description.setText(info.description)
                }
            }
            setTitle(R.string.personal_info)
            setView(input)
            setPositiveButton(resStrConfirm) { dialog, _ ->
                if (input.et_title.text.toString().isBlank()) {
                    Snackbar.make(
                        rootView,
                        R.string.msg_title_cannot_be_blank,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                dialog.dismiss()
                val newInfo = PersonalInfo(
                    id = infoId,
                    personId = friendId,
                    title = input.et_title.text.toString(),
                    description = input.et_description.text.toString()
                )
                callback(newInfo)
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder.create().show()
    }

    companion object {
        private const val ARG_FRIEND_ID = "argFriendId"

        @JvmStatic
        fun newInstance(friendId: Long) =
            FriendDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_FRIEND_ID, friendId)
                }
            }
    }
}
