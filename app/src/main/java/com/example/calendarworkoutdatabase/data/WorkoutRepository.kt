package com.example.calendarworkoutdatabase.data

import io.reactivex.Completable
import io.reactivex.Observable

class WorkoutRepository(private val workoutDAO: WorkoutDAO) {

    val readAllData: Observable<List<WorkoutDate>> = workoutDAO.getAll()

    fun addDate(workoutDate: WorkoutDate): Completable {
        return workoutDAO.addWorkout(workoutDate)
    }
}