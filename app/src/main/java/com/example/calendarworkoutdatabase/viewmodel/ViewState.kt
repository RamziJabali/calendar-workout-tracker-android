package com.example.calendarworkoutdatabase.viewmodel

import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorRes
import com.example.calendarworkoutdatabase.data.WorkoutDate
import java.util.*

data class ViewState(
    var listOfDateEntries: List<DateEntry> = emptyList()
)

data class DateEntry(
    val date: Date,
    val backgroundColor: ColorDrawable,
)