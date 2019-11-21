package com.morshues.connbroandroid.widget

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.morshues.connbroandroid.R
import com.morshues.connbroandroid.util.DateTimeUtils
import java.sql.Date
import java.util.*

class TimeSpinner(
    context: Context,
    attrs: AttributeSet? = null
) : Spinner(context, attrs) {

    private var skipPicker = false
    private var textCache = ""

    init {
        adapter = ArrayAdapter.createFromResource(
            context,
            R.array.personal_event_time,
            android.R.layout.simple_spinner_dropdown_item
        )
        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 5 && (view as? TextView) != null) {
                    view.text = textCache
                    if (skipPicker) {
                        skipPicker = false
                        return
                    }
                    val c = DateTimeUtils.timeToCalender(textCache)
                    val dlg = TimePickerDialog(context,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            textCache = DateTimeUtils.toTimeString(hourOfDay, minute)
                            view.text = textCache
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true
                    )
                    dlg.show()
                }
            }
        }
    }

    override fun setSelection(position: Int, animate: Boolean) {
        val isSameSelected = position == selectedItemPosition
        super.setSelection(position, animate)
        if (isSameSelected) {
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    override fun setSelection(position: Int) {
        val isSameSelected = position == selectedItemPosition
        super.setSelection(position)
        if (isSameSelected) {
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    fun reset() {
        setSelection(0)
    }

    fun setTime(date: Date?) {
        textCache = DateTimeUtils.toTimeString(date)
        skipPicker = true
        setSelection(5)
    }

    fun getTime(): Date? {
        val c = Calendar.getInstance()
        return when (selectedItemPosition) {
            0 -> Date(c.timeInMillis)
            1 -> {
                c.set(Calendar.HOUR_OF_DAY, 8)
                c.set(Calendar.MINUTE, 0)
                Date(c.timeInMillis)
            }
            2 -> {
                c.set(Calendar.HOUR_OF_DAY, 13)
                c.set(Calendar.MINUTE, 0)
                Date(c.timeInMillis)
            }
            3 -> {
                c.set(Calendar.HOUR_OF_DAY, 18)
                c.set(Calendar.MINUTE, 0)
                Date(c.timeInMillis)
            }
            4 -> {
                c.set(Calendar.HOUR_OF_DAY, 20)
                c.set(Calendar.MINUTE, 0)
                Date(c.timeInMillis)
            }
            5 -> {
                val view = selectedView as TextView
                val time = DateTimeUtils.timeToCalender(view.text).timeInMillis
                Date(time)
            }
            else -> null
        }
    }
}