package com.example.calendarworkoutdatabase.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.calendarworkoutdatabase.usecase.UseCase
import com.example.calendarworkoutdatabase.util.isAfterToday
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class ViewModel(application: Application) : AndroidViewModel(application) {
    val viewStateObservable = BehaviorSubject.create<ViewState>()
    private var viewState = ViewState()
    private val useCase by lazy { UseCase(application) }

    @SuppressLint("CheckResult")
    fun selectedDate(date: Date) {
        if (date.isAfterToday()) return
        useCase.getColoredWorkoutDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // We found an existing date
                        { coloredWorkoutDate ->
                            useCase.updateWorkDate(coloredWorkoutDate)
                            getAllWorkoutDates()
                        },
                        // Error
                        {
                            Log.e("TAG", it.toString())
                        },
                        // Complete: We did not find an existing date
                        {
                            useCase.addDate(date)
                            getAllWorkoutDates()
                        }
                )

    }

    @SuppressLint("CheckResult")
    fun getAllWorkoutDates() {
        useCase.getAllColoredWorkoutDates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { list ->
                            viewState.listOfColoredWorkoutDates = list
                            invalidateView()
                            Log.i("ViewModel", "Success retrieving database")
                        },
                        { error -> Log.e("ViewModel", error.localizedMessage) })
    }

    fun deleteAllWorkoutDates() {
        useCase.deleteAllEntries()
        getAllWorkoutDates()
    }

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }
}

