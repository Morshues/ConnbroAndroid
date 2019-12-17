package com.morshues.connbroandroid.ui.event

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.databinding.DialogEventEditingBinding
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.util.InjectorUtils
import com.morshues.connbroandroid.widget.Frequency
import com.morshues.connbroandroid.widget.FrequencyPickerDialog
import com.morshues.connbroandroid.widget.FrequencyPickerViewModel
import java.util.*

class EventEditingDialog : DialogFragment() {
    private val args: EventEditingDialogArgs by navArgs()

    private lateinit var binding: DialogEventEditingBinding
    private val eventViewModel: EventEditingViewModel by viewModels {
        InjectorUtils.provideEventEditingViewModelFactory(
            requireContext(),
            args.userId,
            args.personId,
            args.eventId
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).apply {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_event_editing,
                null,
                false
            )
            eventViewModel.event.observe(requireActivity(), Observer<Event> {
                binding.event = it
            })
            binding.frequency = eventViewModel.frequency
            binding.setEditFrequencyListener {
                FrequencyPickerDialog.newInstance(eventViewModel.frequency)
                    .show(requireFragmentManager(), null)
            }
            setTitle(R.string.personal_event)
            setView(binding.root)
            val resStrConfirm = if (eventViewModel.eventId == 0L) R.string.add else R.string.update
            setPositiveButton(resStrConfirm) { dialog, _ ->
                if (binding.etTitle.text.isBlank()) {
                    return@setPositiveButton
                }

                dialog.dismiss()
                binding.event?.apply {
                    title = binding.etTitle.text.toString()
                    description = binding.etDescription.text.toString()
                    startTime = binding.vStartAt.getDateTime()
                    endTime = Calendar.getInstance()
                    eventViewModel.updateEvent(this)
                } ?: run {
                    val newEvent = Event(
                        title =  binding.etTitle.text.toString(),
                        description = binding.etDescription.text.toString(),
                        startTime = binding.vStartAt.getDateTime(),
                        endTime = Calendar.getInstance()
                    )
                    eventViewModel.addEvent(newEvent)
                }
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.create()
    }
}