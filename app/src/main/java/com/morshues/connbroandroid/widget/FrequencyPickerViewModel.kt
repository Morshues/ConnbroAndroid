package com.morshues.connbroandroid.widget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FrequencyPickerViewModel(freq: Frequency) : ViewModel() {
    var frequency = MutableLiveData(freq)

}