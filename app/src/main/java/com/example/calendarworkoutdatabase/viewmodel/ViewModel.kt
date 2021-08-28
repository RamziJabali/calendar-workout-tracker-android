package com.example.calendarworkoutdatabase.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.calendarworkoutdatabase.data.UseCase
import com.example.calendarworkoutdatabase.data.WorkoutDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class ViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private val colorGreenDrawableRes: ColorDrawable =
                ColorDrawable(Color.GREEN)
        private val coloWhiteDrawableRes: ColorDrawable =
                ColorDrawable(Color.WHITE)
    }

    private var viewState = ViewState()
    private val useCase by lazy { UseCase(application) }

    val viewStateObservable = BehaviorSubject.create<ViewState>()

    @SuppressLint("CheckResult")
    fun addDate(date: Date, didUserWorkout: Boolean) {
        if (date.after(Date())) return

        val workoutDate = getMatchingEntryOrNull(WorkoutDate(date.time, didUserWorkout))

        if (workoutDate != null) {
            useCase.addDate(date, !workoutDate.didUserAttend)
        } else {
            useCase.addDate(date, didUserWorkout)
        }
        getAllWorkoutDates()
    }

    @SuppressLint("CheckResult")
    fun getAllWorkoutDates() {
        useCase.getAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { list ->
                            viewState.listOfDateEntries = list
                            invalidateView()
                            Log.i("ViewModel", "Success retrieving database")
                        },
                        { error -> Log.e("ViewModel", error.localizedMessage) })
    }

    fun deleteAllWorkoutDates() {
        useCase.deleteAllEntries()
        val newListOfDates: MutableList<DateEntry> = mutableListOf()
        for (date in viewState.listOfDateEntries) {
            newListOfDates.add(date.copy(backgroundColor = coloWhiteDrawableRes))
        }
        viewState.listOfDateEntries = newListOfDates
        invalidateView()
    }

    @SuppressLint("CheckResult")
    private fun getMatchingEntryOrNull(userWorkoutDate: WorkoutDate): WorkoutDate? {
        var workoutDate: WorkoutDate? = null
        var boolean: Boolean
        getAllWorkoutDates()
        for (index in viewState.listOfDateEntries) {
            if (index.date.time == userWorkoutDate.date) {
                boolean = index.backgroundColor.color == colorGreenDrawableRes.color //problems are here
                return WorkoutDate(userWorkoutDate.date, boolean)
            }
        }
        return workoutDate
    }

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }
}

