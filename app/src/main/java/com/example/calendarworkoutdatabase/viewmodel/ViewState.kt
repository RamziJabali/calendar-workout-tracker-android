package com.example.calendarworkoutdatabase.viewmodel

import com.example.calendarworkoutdatabase.data.WorkoutDate

data class ViewState(
    var listOfWorkoutDates: List<WorkoutDate> = emptyList(),
    var workoutDate: WorkoutDate = WorkoutDate("",false),
    var didUserWorkOut: Boolean = false,
    var didUserDeleteTable: Boolean = false,
)
