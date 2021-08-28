package com.example.calendarworkoutdatabase.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calendarworkoutdatabase.R
import com.example.calendarworkoutdatabase.viewmodel.ViewModel
import com.example.calendarworkoutdatabase.viewmodel.ViewState
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity(), ViewListener {
    private val viewModel: ViewModel by lazy { ViewModel(application) }
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
    private val deleteAllButton: Button by lazy { findViewById(R.id.deleteAllButton) }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        viewModel.viewStateObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewState ->
                setNewViewState(viewState = viewState)
            }
        viewModel.getAllWorkoutDates()
    }

    override fun setNewViewState(viewState: ViewState) {
        for (dateEntry in viewState.listOfDateEntries) {
            caldroidFragment.setBackgroundDrawableForDate(dateEntry.backgroundColor, dateEntry.date)
        }
        caldroidFragment.refreshView()
    }

    private fun setup() {
        caldroidFragment.caldroidListener = object : CaldroidListener() {
            override fun onSelectDate(date: Date, view: View?) {
                viewModel.addDate(date, true)
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
