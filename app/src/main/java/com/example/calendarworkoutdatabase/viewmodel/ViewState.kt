package com.example.calendarworkoutdatabase.viewmodel

import com.example.calendarworkoutdatabase.data.WorkoutDate
import java.util.*

data class ViewState(
    var listOfWorkoutDates: List<WorkoutDate> = emptyList(),
    var listOfWorkoutDatesConverted: List<Date> = emptyList(),
    var workoutDate: WorkoutDate = WorkoutDate(0, false),
    var didUserWorkOut: Boolean = false,
    var didUserDeleteTable: Boolean = false,
    var didUserAddWorkoutDate: Boolean = false,
)
