package com.example.calendarworkoutdatabase.data

import io.reactivex.Completable
import io.reactivex.Single

class WorkoutRepository(private val workoutDAO: WorkoutDAO) {

    val readAllData: Single<List<WorkoutDate>> = workoutDAO.getAll()

    fun addDate(workoutDate: WorkoutDate): Completable {
        return workoutDAO.addWorkout(workoutDate)
    }
}