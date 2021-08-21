package com.example.calendarworkoutdatabase.viewmodel

import com.example.calendarworkoutdatabase.data.WorkoutDate

data class ViewState(
    var listOfWorkoutDates: List<WorkoutDate> = emptyList(),
    var workout: WorkoutDate = WorkoutDate(0,""),
    var didUserWorkOut: Boolean = false,
    var didUserDeleteTable: Boolean = false
)
