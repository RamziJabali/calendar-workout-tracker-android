package com.example.calendarworkoutdatabase.usecase

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import com.example.calendarworkoutdatabase.repo.WorkoutDatabase
import com.example.calendarworkoutdatabase.repo.WorkoutDate
import com.example.calendarworkoutdatabase.repo.WorkoutRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Date

class UseCase(application: Application) {
    companion object {
        private val hasNotWorkedOutColor: ColorDrawable =
            ColorDrawable(Color.RED)
        private val hasWorkedOutColor: ColorDrawable =
            ColorDrawable(Color.GREEN)
    }

    private val database by lazy { WorkoutDatabase.getInstance(application.applicationContext) }
    private val repository by lazy { WorkoutRepository(database.workoutDAO()) }

    fun getAllColoredWorkoutDates(): Observable<List<ColoredWorkoutDate>> =
        repository.getAllWorkoutDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfWorkoutDate ->
                return@map listOfWorkoutDate.map { getColoredWorkoutDateFromWorkoutDate(it)  }
            }


    fun getColoredWorkoutDate(date: Date): Maybe<ColoredWorkoutDate> =
        repository.getWorkoutDate(date.time)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { workoutDate ->
                getColoredWorkoutDateFromWorkoutDate(workoutDate)
            }


    fun addDate(date: Date) {
        repository.addWorkoutDate(WorkoutDate(date.time, true))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        Log.i("UseCase", "Success Adding Data to database")
    }

    fun updateWorkDate(coloredWorkoutDate: ColoredWorkoutDate) {
        val workOutBool = coloredWorkoutDate.backgroundColor == hasWorkedOutColor
        repository.addWorkoutDate(WorkoutDate(coloredWorkoutDate.date.time, !workOutBool))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        Log.i("UseCase", "Success Adding Data to database")
    }

    fun deleteAllEntries() {
        repository.deleteAllWorkoutDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        Log.i("UseCase", "Success deleting all data from database")
    }

    private fun getColoredWorkoutDateFromWorkoutDate(workoutDate: WorkoutDate): ColoredWorkoutDate =
        ColoredWorkoutDate(
            Date(workoutDate.date),
            if (workoutDate.didUserAttend) hasWorkedOutColor else hasNotWorkedOutColor
        )

}