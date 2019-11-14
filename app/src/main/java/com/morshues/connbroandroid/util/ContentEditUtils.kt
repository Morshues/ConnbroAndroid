package com.morshues.connbroandroid.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import com.morshues.connbroandroid.R
import kotlinx.android.synthetic.main.dialog_personal_info.view.*

object ContentEditUtils {

    fun editTextDialog(
        context: Context,
        oriString: String,
        titleStrId: Int,
        isSingleLine: Boolean = true,
        callback: (edited: String) -> Unit
    ) {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(titleStrId)
            val input = EditText(context)
            input.setText(oriString)
            input.setSingleLine(isSingleLine)
            setView(input)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                callback(input.text.toString())
            }
        }
        builder.create().show()
    }

    fun addPersonalInfoDialog(
        activity: Activity,
        callback: (title: String, description: String) -> Unit
    ) {
        val builder = AlertDialog.Builder(activity).apply {
            val input = activity.layoutInflater.inflate(R.layout.dialog_personal_info, null)
            setView(input)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                callback(input.et_title.text.toString(), input.et_description.text.toString())
            }
        }
        builder.create().show()
    }
}