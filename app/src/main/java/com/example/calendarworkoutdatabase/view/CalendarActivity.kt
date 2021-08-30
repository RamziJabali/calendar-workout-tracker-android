package com.example.calendarworkoutdatabase.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.calendarworkoutdatabase.R
import com.example.calendarworkoutdatabase.viewmodel.ViewModel
import com.example.calendarworkoutdatabase.viewmodel.ViewState
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CalendarActivity : AppCompatActivity(), ViewListener {

    private val viewModel: ViewModel by viewModel()
    private val deleteAllButton: Button by lazy { findViewById(R.id.deleteAllButton) }
    private val caldroidFragment: CaldroidFragment by lazy {
        val calendar = Calendar.getInstance()
        CaldroidFragment().apply {
            arguments = Bundle().apply {
                putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1)
                putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR))
                putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false)
            }
        }
    }
    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        compositeDisposable.add(viewModel.viewStateObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewState ->
                setNewViewState(viewState = viewState)
            })
        viewModel.getAllWorkoutDates()
    }

    override fun setNewViewState(viewState: ViewState) {
        caldroidFragment.apply {
            viewState.listOfColoredWorkoutDates.forEach { dateEntry ->
                setBackgroundDrawableForDate(dateEntry.backgroundColor, dateEntry.date)
            }
            refreshView()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun setup() {
        caldroidFragment.caldroidListener = object : CaldroidListener() {
            override fun onSelectDate(date: Date, view: View?) {
                viewModel.selectedDate(date)
            }
        }
        deleteAllButton.setOnClickListener {
            viewModel.deleteAllWorkoutDates()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.calendarView, caldroidFragment)
            .commit()
    }
}
