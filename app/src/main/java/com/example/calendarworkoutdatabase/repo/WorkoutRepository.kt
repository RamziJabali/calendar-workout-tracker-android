package com.example.calendarworkoutdatabase.repo

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

class WorkoutRepository(private val workoutDAO: WorkoutDAO) {

    fun getAllWorkoutDates(): Observable<List<WorkoutDate>> = workoutDAO.getAll()

    fun getWorkoutDate(date: Long): Maybe<WorkoutDate> = workoutDAO.getByDate(date)

    fun addWorkoutDate(workoutDate: WorkoutDate): Completable = workoutDAO.addUpdateWorkout(workoutDate)

    fun deleteAllWorkoutDates(): Completable = workoutDAO.deleteAll()
}