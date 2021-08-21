package com.example.calendarworkoutdatabase.data

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface WorkoutDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWorkout(workoutDate: WorkoutDate): Completable

    @Query("SELECT * FROM user_workout_schedule")
    fun getAll(): Single<List<WorkoutDate>>

    @Query("SELECT * FROM user_workout_schedule WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): Single<List<WorkoutDate>>

    @Delete
    fun delete(workoutDate: WorkoutDate): Single<Integer>
}