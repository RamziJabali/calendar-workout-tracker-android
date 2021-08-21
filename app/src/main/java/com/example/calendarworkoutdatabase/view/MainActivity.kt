package com.example.calendarworkoutdatabase.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.calendarworkoutdatabase.R
import com.example.calendarworkoutdatabase.viewmodel.ViewModel
import com.example.calendarworkoutdatabase.viewmodel.ViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), ViewListener{
    private val editTextView: EditText by lazy { findViewById(R.id.editTextTextPersonName) }
    val textView: TextView by lazy { findViewById(R.id.textView) }
    private val button: Button by lazy { findViewById(R.id.button) }
    private val button2: Button by lazy { findViewById(R.id.button2) }
    private val button3: Button by lazy { findViewById(R.id.button3) }
    private val viewModel: ViewModel by lazy { ViewModel(application) }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
        viewModel.viewStateObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ viewState ->
                setNewViewState(viewState = viewState)
            }
    }

    override fun setNewViewState(viewState: ViewState) {
        var text = ""
        for (index in viewState.listOfWorkoutDates){
            text+= "${index.date}\n"
        }
        textView.text = text
    }

    private fun setup() {
        button.setOnClickListener {
            viewModel.addDate(editTextView.text.toString())
            Toast.makeText(this, "Added to database", Toast.LENGTH_LONG).show()
        }

        button2.setOnClickListener {
            viewModel.getAllWorkoutDates()
        }

        button3.setOnClickListener {
            viewModel.deleteAllWorkoutDates()
        }
    }


}