package com.morshues.connbroandroid

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.morshues.connbroandroid.util.DateTimeUtils
import java.util.*

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