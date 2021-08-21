package com.example.calendarworkoutdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.calendarworkoutdatabase.data.WorkoutDate
import com.example.calendarworkoutdatabase.data.WorkoutViewModel

class MainActivity : AppCompatActivity() {
    private val editTextView: EditText by lazy { findViewById(R.id.editTextTextPersonName)}
    val textView: TextView by lazy { findViewById(R.id.textView)}
    private val button: Button by lazy { findViewById(R.id.button)}
    private lateinit var viewModel: WorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        button.setOnClickListener{
            if (editTextView.text.toString().isNotEmpty()){
                viewModel.addUser(WorkoutDate(editTextView.text.toString()))
                Toast.makeText(this, "Added to database", Toast.LENGTH_LONG).show()
            }
        }
    }
}