package com.example.calendarworkoutdatabase.data

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import com.example.calendarworkoutdatabase.viewmodel.DateEntry
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class UseCase(application: Application) {
    companion object {
        private val colorRedDrawableRes: ColorDrawable =
            ColorDrawable(Color.RED)
        private val colorGreenDrawableRes: ColorDrawable =
            ColorDrawable(Color.GREEN)
    }

    private val database by lazy { WorkoutDatabase.getInstance(application.applicationContext) }
    private val repository by lazy { WorkoutRepository(database.workoutDAO()) }

    fun getAllData(): Observable<List<DateEntry>> =
        repository.readAllData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { list ->
                convertListLongDateEntriesToDate(list)
            }


    fun addDate(date: Date, boolean: Boolean) {
        repository.addDate(WorkoutDate(date.time, boolean))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        Log.i("UseCase", "Success Adding Data to database")
    }

    fun deleteAllEntries() {
        repository.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        Log.i("UseCase", "Success deleting all data from database")
    }


    private fun convertListLongDateEntriesToDate(listWorkoutDates: List<WorkoutDate>): List<DateEntry> {
        val list: MutableList<DateEntry> = mutableListOf()
        for (workout in listWorkoutDates) {
            if (workout.didUserAttend) {
                list.add(DateEntry(Date(workout.date), colorGreenDrawableRes))
            } else {
                list.add(DateEntry(Date(workout.date), colorRedDrawableRes))
            }
        }
        return list
    }
}