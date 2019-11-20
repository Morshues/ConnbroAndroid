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
import java.util.*


class TimeSpinner(
    context: Context,
    attrs: AttributeSet? = null
) : Spinner(context, attrs) {

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
                if (position == 5) {
                    view as TextView
                    val c = DateTimeUtils.timeToCalender(view.text)
                    val dlg = TimePickerDialog(context,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            DateTimeUtils.toTimeString(hourOfDay, minute)
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

    fun getDate(): TimeData? {
        return when (selectedItemPosition) {
            0 -> {
                val c = Calendar.getInstance()
                TimeData(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
            }
            1 -> TimeData(7, 0)
            2 -> TimeData(12, 0)
            3 -> TimeData(17, 0)
            4 -> TimeData(19, 0)
            5 -> {
                val view = selectedView as TextView
                val c = DateTimeUtils.timeToCalender(view.text)
                TimeData(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
            }
            else -> null
        }
    }

    data class TimeData(val hour: Int, val min: Int)
}