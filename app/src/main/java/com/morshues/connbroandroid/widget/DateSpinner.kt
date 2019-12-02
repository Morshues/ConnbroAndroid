package com.morshues.connbroandroid.widget

import android.app.DatePickerDialog
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

class DateSpinner(
    context: Context,
    attrs: AttributeSet? = null
) : Spinner(context, attrs) {

    private var skipPicker = false
    private var textCache = ""

    init {
        adapter = ArrayAdapter.createFromResource(
            context,
            R.array.personal_event_date,
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
                if (position == 3 && (view as? TextView) != null) {
                    view.text = textCache
                    if (skipPicker) {
                        skipPicker = false
                        return
                    }
                    val c = DateTimeUtils.dateToCalender(textCache) ?: Calendar.getInstance()
                    val dlg = DatePickerDialog(context,
                        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            textCache = DateTimeUtils.toDateString(year, month, dayOfMonth)
                            view.text = textCache
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
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

    fun setDate(calendar: Calendar?) {
        textCache = DateTimeUtils.toDateString(calendar)
        skipPicker = true
        setSelection(3)
    }

    fun getDate(): Calendar? {
        when (selectedItemPosition) {
            0 -> return Calendar.getInstance()
            1 -> {
                val c = Calendar.getInstance()
                c.add(Calendar.DAY_OF_MONTH, 1)
                return c
            }
            2 -> {
                val c = Calendar.getInstance()
                c.add(Calendar.DAY_OF_MONTH, -1)
                return c
            }
            3 -> {
                return DateTimeUtils.dateToCalender(textCache)
            }
        }
        return null
    }
}