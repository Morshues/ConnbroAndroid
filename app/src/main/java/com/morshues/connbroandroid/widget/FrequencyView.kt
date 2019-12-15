package com.morshues.connbroandroid.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class FrequencyView(
    context: Context,
    attrs: AttributeSet? = null
) : TextView(context, attrs) {

    var frequency: Frequency = Frequency()

}
