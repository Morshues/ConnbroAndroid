package com.morshues.connbroandroid.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
import java.util.*

class FriendDetailFragment : Fragment() {
    private val args: FriendDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentFriendDetailBinding
    private val viewModel: FriendDetailViewModel by viewModels {
        InjectorUtils.provideFriendDetailViewModelFactory(
            requireContext(),
            args.userId,
            args.friendId
        )
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

        val eventsAdapter = PersonalEventsAdapter(viewModel.userId, viewModel.friendId)
        binding.rvEvents.adapter = eventsAdapter
        val infoAdapter = PersonalInfoAdapter(viewModel.userId, viewModel.friendId)
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
                val direction =
                    FriendDetailFragmentDirections.actionFriendDetailFragmentToInfoEditingDialog(
                        viewModel.userId,
                        viewModel.friendId,
                        0
                    )
                findNavController().navigate(direction)
                true
            }
            R.id.action_add_event -> {
                val direction =
                    FriendDetailFragmentDirections.actionFriendDetailFragmentToEventEditingDialog(
                        viewModel.userId,
                        viewModel.friendId,
                        0
                    )
                findNavController().navigate(direction)
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
                val event = eventsAdapter.getEventAt(viewHolder.adapterPosition)
                deleteEvent(event)
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
                val personalInfo = infoAdapter.getInfoAt(viewHolder.adapterPosition)
                deletePersonalInfo(personalInfo)
            }
        }).attachToRecyclerView(binding.rvPersonInfo)
    }

    private var mLastDeletedEvent: Event? = null
    private fun deleteEvent(event: Event) {
        mLastDeletedEvent = event
        viewModel.deleteEvent(event)

        Snackbar.make(binding.root, R.string.deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) { viewModel.restoreEvent(mLastDeletedEvent!!) }
            .show()
    }

    private var mLastDeletedInfo: PersonalInfo? = null
    private fun deletePersonalInfo(info: PersonalInfo) {
        mLastDeletedInfo = info
        viewModel.deleteInfo(info)

        Snackbar.make(binding.root, R.string.deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) { viewModel.restoreInfo(mLastDeletedInfo!!) }
            .show()
    }
}
