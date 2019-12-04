package com.morshues.connbroandroid

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.morshues.connbroandroid.util.DateTimeUtils
import com.morshues.connbroandroid.widget.DateTimeView
import java.util.*

@BindingAdapter("dateTime")
fun bindDateTime(view: DateTimeView, c: Calendar?) {
    view.setDateTime(c)
}

@BindingAdapter("dateText")
fun bindDateText(view: TextView, c: Calendar?) {
    if (c == null) {
        view.text = ""
    } else {
        view.text = DateTimeUtils.toDateString(c)
    }
}

@BindingAdapter("dateRelativeText")
fun bindDateRelativeText(view: TextView, c: Calendar?) {
    if (c == null) {
        view.text = ""
    } else {
        view.text = DateUtils.getRelativeTimeSpanString(c.timeInMillis)
    }
}