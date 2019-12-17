package com.morshues.connbroandroid.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.databinding.DialogFrequencyPickerBinding
import com.morshues.connbroandroid.util.InjectorUtils
import java.text.DateFormatSymbols
import java.util.*

class FrequencyPickerDialog : DialogFragment() {

    private lateinit var binding: DialogFrequencyPickerBinding
    private val viewModel: FrequencyPickerViewModel by viewModels {
        InjectorUtils.provideFrequencyPickerViewModelFactory(arguments!!.getParcelable(ARG_FREQUENCY)!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext()).apply {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_frequency_picker,
                null,
                false
            )
            setView(binding.root)
            subscribeUI(binding)
        }.create()
    }
    private fun subscribeUI(binding: DialogFrequencyPickerBinding) {
        binding.frequency = viewModel.frequency
        binding.onTypeSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.frequency.setType(position)
            }
        }
        val date = viewModel.frequency.defaultCalendar

        val dfs = DateFormatSymbols()
        val month = dfs.months[date.get(Calendar.MONTH)]
        val weekOfMonth = ordinal(date.get(Calendar.DAY_OF_WEEK_IN_MONTH))
        val dayOfWeek = dfs.weekdays[date.get(Calendar.DAY_OF_WEEK)]
        binding.textDayOfWeekOfMonth =
            resources.getString(R.string.freq_same_day_of_week_of_month, weekOfMonth, dayOfWeek)
        binding.textDayOfWeekOfMonthOfYear = resources.getString(
            R.string.freq_same_day_of_week_of_month_of_year,
            month,
            weekOfMonth,
            dayOfWeek
        )
        binding.setOnSaveListener {
            onResult(viewModel.frequency)
            this.dismiss()
        }
    }

    var onResult: (freq: Frequency) -> Unit = {}

    // TODO: This converter only use for English. Need to replace it for multi language supporting.
    private fun ordinal(i: Int): String? {
        val sufixes =
            arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
        return when (i % 100) {
            11, 12, 13 -> "${i}th"
            else -> "$i${sufixes[i % 10]}"
        }
    }

    companion object {
        private const val ARG_FREQUENCY = "argFrequency"

        @JvmStatic
        fun newInstance(freq: Frequency) =
            FrequencyPickerDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FREQUENCY, freq)
                }
            }
    }
}

@BindingAdapter("selected")
fun bindSelected(textView: TextView, boolean: Boolean) {
    textView.isSelected = boolean
}

@BindingAdapter("frequency")
fun bindPeriod(textView: TextView, freq: Frequency) {
    textView.text = when (freq.type) {
        Frequency.Type.DAILY ->
            textView.resources.getQuantityString(R.plurals.freq_picker_every_day, freq.interval)
        Frequency.Type.WEEKLY ->
            textView.resources.getQuantityString(R.plurals.freq_picker_every_week, freq.interval)
        Frequency.Type.MONTHLY ->
            textView.resources.getQuantityString(R.plurals.freq_picker_every_month, freq.interval)
        Frequency.Type.YEARLY ->
            textView.resources.getQuantityString(R.plurals.freq_picker_every_year, freq.interval)
    }
}