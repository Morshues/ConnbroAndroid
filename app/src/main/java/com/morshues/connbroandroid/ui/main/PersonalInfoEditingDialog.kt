package com.morshues.connbroandroid.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.databinding.DialogPersonalInfoEditingBinding
import com.morshues.connbroandroid.db.model.PersonalInfo
import com.morshues.connbroandroid.util.InjectorUtils

class PersonalInfoEditingDialog : DialogFragment() {
    private val args: PersonalInfoEditingDialogArgs by navArgs()

    private lateinit var binding: DialogPersonalInfoEditingBinding
    private val viewModel: PersonalInfoEditingViewModel by viewModels {
        InjectorUtils.provideInfoEditingViewModelFactory(
            requireContext(),
            args.userId,
            args.personId,
            args.personalInfoId
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).apply {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_personal_info_editing,
                null,
                false
            )
            viewModel.info.observe(requireActivity(), Observer<PersonalInfo> {
                binding.personalInfo = it
            })
            setTitle(R.string.personal_info)
            setView(binding.root)
            val resStrConfirm = if (viewModel.infoId == 0L) R.string.add else R.string.update
            setPositiveButton(resStrConfirm) { dialog, _ ->
                if (binding.etTitle.text.isBlank()) {
                    return@setPositiveButton
                }

                dialog.dismiss()
                binding.personalInfo?.apply {
                    title = binding.etTitle.text.toString()
                    description = binding.etDescription.text.toString()
                    viewModel.updatePersonalInfo(this)
                } ?: run {
                    val newPersonalInfo = PersonalInfo(
                        title =  binding.etTitle.text.toString(),
                        description = binding.etDescription.text.toString()
                    )
                    viewModel.addPersonalInfo(newPersonalInfo)
                }
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.create()
    }
}