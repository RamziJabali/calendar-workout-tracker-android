package com.example.calendarworkoutdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.calendarworkoutdatabase.data.WorkoutDatabase
import com.example.calendarworkoutdatabase.data.WorkoutDate
import com.example.calendarworkoutdatabase.data.WorkoutRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private val editTextView: EditText by lazy { findViewById(R.id.editTextTextPersonName) }
    val textView: TextView by lazy { findViewById(R.id.textView) }
    private val button: Button by lazy { findViewById(R.id.button) }
    private val button2: Button by lazy { findViewById(R.id.button2) }
    private val database by lazy { WorkoutDatabase.getInstance(this) }
    private val repository by lazy { WorkoutRepository(database.workoutDAO()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            if (editTextView.text.toString().isNotEmpty()) {
                repository.addDate(WorkoutDate(0, editTextView.text.toString()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                Toast.makeText(this, "Added to database", Toast.LENGTH_LONG).show()
            }
        }

        button2.setOnClickListener {
            repository.readAllData
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { workoutList -> textView.text = workoutList[0].date },
                            { error -> Log.e("Failed", error.localizedMessage) })
        }
    }
}