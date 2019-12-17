package com.morshues.connbroandroid.widget

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Frequency(
    private var _enable: Boolean = true,
    private var _type: Type = Type.DAILY,
    private var _extra: Int = (0x1100).toInt(),
    private var _interval: Int = 1,
    private var _repeatTime: Int = 0,
    private var _isUnlimited: Boolean = true
) : BaseObservable(), Parcelable {

    @IgnoredOnParcel
    val defaultCalendar = GregorianCalendar()
    constructor(defaultTimestamp: Long) : this() {
        defaultCalendar.timeInMillis = defaultTimestamp
        val dayOfWeek = defaultCalendar.get(Calendar.DAY_OF_WEEK) - 1
        toggleBit(dayOfWeek)
    }

    var enable: Boolean
        @Bindable get() = _enable
        set(value) {
            _enable = value
            notifyPropertyChanged(BR.enable)
        }

    var type: Type
        @Bindable get() = _type
        set(value) {
            _type = value
            notifyChange()
        }

    fun setType(num: Int) {
        type = when(num) {
            0 -> Type.DAILY
            1 -> Type.WEEKLY
            2 -> Type.MONTHLY
            3 -> Type.YEARLY
            else -> return
        }
    }

    fun toggleBit(index: Int) {
        val ref = 0x1 shl index
        _extra = _extra xor ref
        notifyChange()
    }

    val monday: Boolean
        @Bindable get() = (_extra and Weekly.MONDAY.num) == Weekly.MONDAY.num

    val tuesday: Boolean
        @Bindable get() = (_extra and Weekly.TUESDAY.num) == Weekly.TUESDAY.num

    val wednesday: Boolean
        @Bindable get() = (_extra and Weekly.WEDNESDAY.num) == Weekly.WEDNESDAY.num

    val thursday: Boolean
        @Bindable get() = (_extra and Weekly.THURSDAY.num) == Weekly.THURSDAY.num

    val friday: Boolean
        @Bindable get() = (_extra and Weekly.FRIDAY.num) == Weekly.FRIDAY.num

    val saturday: Boolean
        @Bindable get() = (_extra and Weekly.SATURDAY.num) == Weekly.SATURDAY.num

    val sunday: Boolean
        @Bindable get() = (_extra and Weekly.SUNDAY.num) == Weekly.SUNDAY.num

    fun setMonthlyExtra(extra: Monthly) {
        _extra = (_extra or 0x0f00) and (extra.num or 0xf0ff)
        notifyChange()
    }

    val monthlyExtra: Int
        @Bindable get() = _extra and 0x0f00

    fun setYearlyExtra(extra: Yearly) {
        _extra = (_extra or 0xf000) and (extra.num or 0x0fff)
        notifyChange()
    }

    val yearlyExtra: Int
        @Bindable get() = _extra and 0xf000

    var interval: Int
        get() = _interval
        set(value) {
            _interval = value
            notifyChange()
        }

    var intervalStr: String
        @Bindable get() = _interval.toString()
        set(value) {
            _interval = value.toIntOrNull() ?: 1
            notifyChange()
        }

    var repeatTime: Int
        get() = _repeatTime
        set(value) {
            _repeatTime = value
            notifyChange()
        }

    enum class Type { DAILY, WEEKLY, MONTHLY, YEARLY }

    enum class Weekly(val num: Int) {
        SUNDAY(0x0001),
        MONDAY(0x0002),
        TUESDAY(0x0004),
        WEDNESDAY(0x0008),
        THURSDAY(0x0010),
        FRIDAY(0x0020),
        SATURDAY(0x0040)
    }
    enum class Monthly(val num: Int) {
        SAME_DAY(0x0100),
        SAME_DAY_OF_WEEK(0x0200)
    }
    enum class Yearly(val num: Int) {
        SAME_DAY(0x1000),
        SAME_DAY_OF_WEEK_OF_MONTH(0x2000)
    }
}