package com.example.calendarworkoutdatabase.data

import androidx.lifecycle.LiveData

class WorkoutRepository(private val workoutDAO: WorkoutDAO) {

    val readAllData: LiveData<List<WorkoutDate>> = workoutDAO.readAllData()

    fun addDate(workoutDate: WorkoutDate) {
        workoutDAO.addWorkout(workoutDate)
    }
}