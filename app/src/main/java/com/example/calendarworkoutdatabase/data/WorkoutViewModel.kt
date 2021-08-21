package com.example.calendarworkoutdatabase.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val readAllData: LiveData<List<WorkoutDate>>
    private val repository: WorkoutRepository

    init {
        val workoutDAO = WorkoutDatabase.getDatabase(application).workoutDAO()
        repository = WorkoutRepository(workoutDAO)
        readAllData = repository.readAllData
    }

    fun addUser(workoutDate: WorkoutDate){
            repository.addDate(workoutDate)
    }
}