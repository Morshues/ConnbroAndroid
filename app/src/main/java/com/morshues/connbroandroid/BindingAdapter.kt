package com.morshues.connbroandroid

import android.text.format.DateUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
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

@BindingAdapter("onItemSelected")
fun bindOnItemSelected(spinner: Spinner, itemSelectedListener: AdapterView.OnItemSelectedListener) {
    spinner.onItemSelectedListener = itemSelectedListener
}

@BindingAdapter("enabled")
fun bindEnabled(spinner: Spinner, boolean: Boolean) {
    spinner.isEnabled = boolean
}

@BindingAdapter("enabled")
fun bindEnabled(group: Group, boolean: Boolean) {
    group.isEnabled = boolean
    val refIds = group.referencedIds
    val layout = group.parent as ConstraintLayout
    for (id in refIds) {
        layout.findViewById<View>(id).isEnabled = boolean
    }
}
