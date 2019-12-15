package com.morshues.connbroandroid.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.morshues.connbroandroid.R

class FrequencyView(
    context: Context,
    attrs: AttributeSet? = null
) : TextView(context, attrs) {

    var frequency: Frequency? = null
        set(value) {
            field = value
            if (field?.enable == true) {
                text = when (field!!.type) {
                    Frequency.Type.DAILY -> resources.getString(R.string.personal_event_freq_daily)
                    Frequency.Type.WEEKLY -> resources.getString(R.string.personal_event_freq_weekly)
                    Frequency.Type.MONTHLY -> resources.getString(R.string.personal_event_freq_monthly)
                    Frequency.Type.YEARLY -> resources.getString(R.string.personal_event_freq_yearly)
                }
                return
            }
            text = resources.getString(R.string.personal_event_freq_once)
        }

    init {
        text = resources.getString(R.string.personal_event_freq_once)
    }
}

@BindingAdapter("frequency")
fun setFrequencyToFrequencyView(view: FrequencyView, frequency: Frequency) {
    view.frequency = frequency
}