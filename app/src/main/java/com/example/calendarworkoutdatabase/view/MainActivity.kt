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
    companion object {
        private val colorRedDrawableRes: ColorDrawable =
            ColorDrawable(Color.RED)
        private val colorGreenDrawableRes: ColorDrawable =
            ColorDrawable(Color.GREEN)
    }

    private val viewModel: ViewModel by lazy { ViewModel(application) }
    private val caldroidFragment: CaldroidFragment by lazy { CaldroidFragment() }
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
    }

    override fun setNewViewState(viewState: ViewState) {
        if (viewState.didUserAddWorkoutDate) {
            if (viewState.workoutDate.didUserAttend) {
                caldroidFragment.setBackgroundDrawableForDate(colorGreenDrawableRes,Date(viewState.workoutDate.date))
            } else {
                caldroidFragment.setBackgroundDrawableForDate(colorRedDrawableRes,Date(viewState.workoutDate.date))
            }
            Log.i("Added Date -> DB: ", Date(viewState.workoutDate.date).toString())
        } else if (viewState.didUserDeleteTable) {
            caldroidFragment.clearBackgroundDrawableForDates(viewState.listOfWorkoutDatesConverted)
            Toast.makeText(this, "Deleted All Entries Successfully", Toast.LENGTH_SHORT).show()
        }
        caldroidFragment.refreshView()
    }

    private fun setup() {
        val calendar = Calendar.getInstance()
        var args = Bundle()
        args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1)
        args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR))
        caldroidFragment.arguments = args
        supportFragmentManager.beginTransaction()
            .replace(R.id.calendarView, caldroidFragment)
            .commit()

        caldroidFragment.caldroidListener = object : CaldroidListener() {
            override fun onSelectDate(date: Date?, view: View?) {
                if (date != null) {
                    viewModel.addDate(date.time, true)
                }
            }
        }
        deleteAllButton.setOnClickListener {
            viewModel.getAllWorkoutDates()
            viewModel.deleteAllWorkoutDates()
        }
    }

}
