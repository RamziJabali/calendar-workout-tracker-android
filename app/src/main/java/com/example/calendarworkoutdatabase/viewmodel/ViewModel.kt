package com.example.calendarworkoutdatabase.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.calendarworkoutdatabase.data.WorkoutDatabase
import com.example.calendarworkoutdatabase.data.WorkoutDate
import com.example.calendarworkoutdatabase.data.WorkoutRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class ViewModel(application: Application) : AndroidViewModel(application) {
    private var viewState = ViewState()
    val viewStateObservable = BehaviorSubject.create<ViewState>()

    private val database by lazy { WorkoutDatabase.getInstance(application.applicationContext) }
    private val repository by lazy { WorkoutRepository(database.workoutDAO()) }

    @SuppressLint("CheckResult")
    fun addDate(date: Long, didUserWorkout: Boolean) {
        if (doesThisEntryExist(WorkoutDate(date, didUserWorkout))) {
            viewState.workoutDate =
                WorkoutDate(date = date, didUserAttend = !viewState.workoutDate.didUserAttend)
        } else {
            viewState.workoutDate = WorkoutDate(date, didUserWorkout)
        }
        repository.addDate(viewState.workoutDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.v("ViewModel", "Successfully Added date to DataBase") },
                { error -> Log.e("Adding date to DB", error.localizedMessage) }
            )
        viewState = viewState.copy(didUserAddWorkoutDate = true, didUserDeleteTable = false)
        invalidateView()
    }

    @SuppressLint("CheckResult")
    fun getAllWorkoutDates() {
        repository.readAllData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listWorkoutDates ->
                    viewState.listOfWorkoutDatesConverted = convertTimeToDate(listWorkoutDates)
                    viewState.listOfWorkoutDates = listWorkoutDates
                },
                { error -> Log.e("DataBase", error.localizedMessage) })
        viewState = viewState.copy(didUserDeleteTable = false, didUserAddWorkoutDate = false)
        invalidateView()
    }

    @SuppressLint("CheckResult")
    fun deleteAllWorkoutDates() {
        repository.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        viewState = viewState.copy(didUserAddWorkoutDate = false, didUserDeleteTable = true)
        invalidateView()
    }

    @SuppressLint("CheckResult")
    private fun doesThisEntryExist(userWorkoutDate: WorkoutDate): Boolean {
        repository.readAllData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listWorkoutDates ->
                    viewState.listOfWorkoutDates = listWorkoutDates
                },
                { error -> Log.e("DataBase", error.localizedMessage) })
        for (index in viewState.listOfWorkoutDates) {
            if (index.date == userWorkoutDate.date) return true
        }
        return false
    }

    private fun convertTimeToDate(allDates: List<WorkoutDate>): List<Date> {
        var listOfDates: MutableList<Date> = mutableListOf()
        for (index in allDates) {
            listOfDates.add(Date(index.date))
        }
        return listOfDates
    }

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }
}

