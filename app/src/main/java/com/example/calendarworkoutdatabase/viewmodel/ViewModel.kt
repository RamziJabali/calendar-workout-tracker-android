package com.example.calendarworkoutdatabase.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
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

    fun addDate(text: String) {
        repository.addDate(WorkoutDate(0, text))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        invalidateView()
    }

    @SuppressLint("CheckResult")
    fun getAllWorkoutDates(){
        repository.readAllData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {listWorkoutDates -> viewState.listOfWorkoutDates = listWorkoutDates
                invalidateView()},
                {error -> Log.e("DataBase", error.localizedMessage)})
        invalidateView()
    }

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }


}
