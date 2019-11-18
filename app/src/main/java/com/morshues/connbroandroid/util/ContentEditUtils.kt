package com.morshues.connbroandroid.util

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

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

}