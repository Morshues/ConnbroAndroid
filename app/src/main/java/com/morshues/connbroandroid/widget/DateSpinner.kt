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
import java.sql.Date
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
                    val c = DateTimeUtils.dateToCalender(textCache)
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

    fun setDate(date: Date?) {
        textCache = DateTimeUtils.toDateString(date)
        skipPicker = true
        setSelection(3)
    }

    fun getDate(): Date? {
        when (selectedItemPosition) {
            0 -> return Date(System.currentTimeMillis())
            1 -> return Date(System.currentTimeMillis() + A_DAY_IN_MILLISECOND)
            2 -> return Date(System.currentTimeMillis() - A_DAY_IN_MILLISECOND)
            3 -> {
                return DateTimeUtils.toSqlDate(textCache)
            }
        }
        return null
    }

    companion object {
        private const val A_DAY_IN_MILLISECOND = 86_400_000L
    }
}