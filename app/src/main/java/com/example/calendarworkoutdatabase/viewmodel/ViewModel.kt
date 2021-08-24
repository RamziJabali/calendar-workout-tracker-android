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

class ViewModel(application: Application) : AndroidViewModel(application) {
    private var viewState = ViewState()
    val viewStateObservable = BehaviorSubject.create<ViewState>()

    private val database by lazy { WorkoutDatabase.getInstance(application.applicationContext) }
    private val repository by lazy { WorkoutRepository(database.workoutDAO()) }

    fun addDate(date: String, didUserWorkout: Boolean) {
        if (doesThisEntryExist(WorkoutDate(date, didUserWorkout))) {
            viewState.workoutDate = WorkoutDate(date = date, didUserAttend = !viewState.workoutDate.didUserAttend)
        } else {
            viewState.workoutDate = WorkoutDate(date, didUserWorkout)
        }
        repository.addDate(viewState.workoutDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        invalidateView()
    }

    @SuppressLint("CheckResult")
    fun getAllWorkoutDates() {
        repository.readAllData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { listWorkoutDates ->
                            viewState.listOfWorkoutDates = listWorkoutDates
                            invalidateView()
                        },
                        { error -> Log.e("DataBase", error.localizedMessage) })
        viewState = viewState.copy(didUserDeleteTable = false)
        invalidateView()
    }

    fun deleteAllWorkoutDates() {
        repository.deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        viewState = viewState.copy(didUserDeleteTable = true)
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
                            invalidateView()
                        },
                        { error -> Log.e("DataBase", error.localizedMessage) })
        for (index in viewState.listOfWorkoutDates) {
            if (index.date == userWorkoutDate.date) return true
        }
        return false
    }

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }
}
