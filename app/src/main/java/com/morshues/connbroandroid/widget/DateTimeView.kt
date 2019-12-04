package com.morshues.connbroandroid.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.util.DateTimeUtils
import kotlinx.android.synthetic.main.view_datetime.view.*
import java.util.*

class DateTimeView(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        val view = inflate(getContext(), R.layout.view_datetime, null)
        addView(view)
    }

    fun reset() {
        spn_date.reset()
        spn_time.reset()
    }

    fun setDateTime(datetime: Calendar?) {
        if (datetime == null) {
            return
        }
        spn_date.setDate(datetime)
        spn_time.setTime(datetime)
    }

    fun getDateTime(): Calendar? {
        return DateTimeUtils.combineDateTime(spn_date.getDate(), spn_time.getTime())
    }
}