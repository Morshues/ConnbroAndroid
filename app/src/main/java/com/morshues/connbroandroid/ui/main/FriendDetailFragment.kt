package com.morshues.connbroandroid.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.databinding.FragmentFriendDetailBinding
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.db.model.PersonalInfo
import com.morshues.connbroandroid.util.ContentEditUtils
import com.morshues.connbroandroid.util.DateTimeUtils
import com.morshues.connbroandroid.util.InjectorUtils
import kotlinx.android.synthetic.main.partial_event_editing.view.*
import kotlinx.android.synthetic.main.partial_personal_info_editing.view.et_description
import kotlinx.android.synthetic.main.partial_personal_info_editing.view.et_title
import java.util.*

class FriendDetailFragment : Fragment() {
    private val args: FriendDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentFriendDetailBinding
    private val viewModel: FriendDetailViewModel by viewModels {
        InjectorUtils.provideFriendDetailViewModelFactory(requireContext(), args.friendId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendDetailBinding.inflate(inflater, container, false)

        binding.setTextEditListener { v ->
            if (v !is TextView) return@setTextEditListener
            var resTitle = 0
            var callback: (String) -> Unit = {}
            val lytDetail = binding.lytDetail
            when (v.id) {
                lytDetail.tvLastName.id -> {
                    resTitle = R.string.last_name
                    callback = { viewModel.updateLastName(it) }
                }
                lytDetail.tvMidName.id -> {
                    resTitle = R.string.mid_name
                    callback = { viewModel.updateMidName(it) }
                }
                lytDetail.tvFirstName.id -> {
                    resTitle = R.string.first_name
                    callback = { viewModel.updateFirstName(it) }
                }
                lytDetail.tvNickName.id -> {
                    resTitle = R.string.nick_name
                    callback = { viewModel.updateNickName(it) }
                }
                lytDetail.tvNote.id -> {
                    resTitle = R.string.note
                    callback = { viewModel.updateNote(it) }
                }
            }
            ContentEditUtils.editTextDialog(
                requireContext(),
                v.text.toString(),
                resTitle,
                callback = callback
            )
        }

        binding.setDateEditListener { v ->
            if (v !is TextView) return@setDateEditListener
            val c = DateTimeUtils.dateToCalender(v.text) ?: Calendar.getInstance()
            val dlg = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.updateBirthday(DateTimeUtils.dateToCalender(year, month, dayOfMonth))
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
            )
            dlg.show()
        }

        val eventsAdapter = PersonalEventsAdapter()
        binding.rvEvents.adapter = eventsAdapter
        val infoAdapter = PersonalInfoAdapter()
        binding.rvPersonInfo.adapter = infoAdapter

        subscribeUi(eventsAdapter, infoAdapter, binding)

        return binding.root
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

    private fun subscribeUi(
        eventsAdapter: PersonalEventsAdapter,
        infoAdapter: PersonalInfoAdapter,
        binding: FragmentFriendDetailBinding
    ) {
        viewModel.friendData.observe(viewLifecycleOwner, Observer<PersonDetail> {
            binding.friend = it
            eventsAdapter.submitList(it.sortedEvents())
            infoAdapter.submitList(it.sortedInfo())
        })

        eventsAdapter.setOnItemClickListener(object : PersonalEventsAdapter.OnItemClickListener {
            override fun onEventUpdate(event: Event) {
                editEventDialog(event) {
                    viewModel.updateEvent(it)
                }
            }
        })
        infoAdapter.setOnItemClickListener(object : PersonalInfoAdapter.OnItemClickListener {
            override fun onInfoUpdate(info: PersonalInfo) {
                editInfoDialog(info) {
                    viewModel.updateInfo(it)
                }
            }
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
                viewModel.deleteEvent(eventsAdapter.getEventAt(position))
                Snackbar.make(viewHolder.itemView, R.string.deleted, Snackbar.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(binding.rvEvents)

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
                viewModel.deleteInfo(infoAdapter.getInfoAt(position))
                Snackbar.make(viewHolder.itemView, R.string.deleted, Snackbar.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(binding.rvPersonInfo)
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
                        binding.root,
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
                    endTime = Calendar.getInstance()
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
                        binding.root,
                        R.string.msg_title_cannot_be_blank,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                dialog.dismiss()
                val newInfo = PersonalInfo(
                    id = infoId,
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
}
