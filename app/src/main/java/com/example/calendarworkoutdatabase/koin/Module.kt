package com.example.calendarworkoutdatabase.koin

import com.example.calendarworkoutdatabase.repo.WorkoutDatabase
import com.example.calendarworkoutdatabase.repo.WorkoutRepository
import com.example.calendarworkoutdatabase.usecase.UseCase
import com.example.calendarworkoutdatabase.viewmodel.ViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calendarModule = module {
    viewModel { ViewModel(androidApplication(), get()) }
    single { UseCase(get()) }
    single { WorkoutRepository(get<WorkoutDatabase>().workoutDAO()) }
    single { WorkoutDatabase.getInstance(androidApplication().applicationContext) }

}
