package com.example.calendarworkoutdatabase.usecase

import android.graphics.drawable.ColorDrawable
import java.util.Date

data class ColoredWorkoutDate(
    val date: Date,
    val backgroundColor: ColorDrawable,
)