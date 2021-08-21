package com.example.calendarworkoutdatabase.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_workout_schedule")
data class WorkoutDate(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "date") val date: String)
