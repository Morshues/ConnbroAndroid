package com.morshues.connbroandroid

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.sql.Date

@BindingAdapter("dateText")
fun bindDateText(view: TextView, date: Date?) {
    if (date == null) {
        view.text = ""
    } else {
        view.text = date.toString()
    }
}

@BindingAdapter("dateRelativeText")
fun bindDateRelativeText(view: TextView, date: Date?) {
    if (date == null) {
        view.text = ""
    } else {
        view.text = DateUtils.getRelativeTimeSpanString(date.time)
    }
}