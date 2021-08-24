package com.example.calendarworkoutdatabase.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.calendarworkoutdatabase.R
import com.example.calendarworkoutdatabase.time.zoneddatetime.ZonedDateTimes
import com.example.calendarworkoutdatabase.viewmodel.ViewModel
import com.example.calendarworkoutdatabase.viewmodel.ViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), ViewListener {
    private val calendarView: CalendarView by lazy { findViewById(R.id.calendarView) }
    private val viewModel: ViewModel by lazy { ViewModel(application) }

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
    }

    override fun setNewViewState(viewState: ViewState) {
        val tomorrow = ZonedDateTimes.tomorrow.toInstant().toEpochMilli()
        calendarView.setDate(tomorrow, true, true)
    }

    private fun setup() {
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            viewModel.addDate("$year$month$dayOfMonth", true)
            Toast.makeText(this, "Added date successfully", Toast.LENGTH_LONG).show()
        }
    }
}
