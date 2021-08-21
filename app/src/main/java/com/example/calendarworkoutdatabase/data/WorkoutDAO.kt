package com.example.calendarworkoutdatabase.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkoutDAO {
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun addWorkout(workoutDate: WorkoutDate)

    @Query("SELECT * FROM user_workout_schedule ORDER BY date ASC")
    fun readAllData(): LiveData<List<WorkoutDate>>

    @Query("DELETE FROM user_workout_schedule")
    fun deleteAll()
}