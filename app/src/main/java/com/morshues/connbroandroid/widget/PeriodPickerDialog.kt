package com.morshues.connbroandroid.widget

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.morshues.connbroandroid.util.InjectorUtils

class PeriodPickerDialog : DialogFragment() {

    private val viewModel: PeriodPickerViewModel by viewModels {
        InjectorUtils.providePeriodPickerViewModelFactory()
    }

}