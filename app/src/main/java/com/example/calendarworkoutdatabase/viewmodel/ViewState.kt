package com.example.calendarworkoutdatabase.viewmodel

import com.example.calendarworkoutdatabase.usecase.ColoredWorkoutDate

data class ViewState(
    var listOfColoredWorkoutDates: List<ColoredWorkoutDate> = emptyList()
)