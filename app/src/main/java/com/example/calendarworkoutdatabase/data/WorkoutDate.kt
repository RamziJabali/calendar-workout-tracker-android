package com.example.calendarworkoutdatabase.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_workout_schedule")
data class WorkoutDate(
    @PrimaryKey @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "did_user_attend_date") val didUserAttend: Boolean
)
