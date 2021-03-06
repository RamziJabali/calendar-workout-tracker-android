package com.example.calendarworkoutdatabase

import android.app.Application
import com.example.calendarworkoutdatabase.koin.calendarModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CalendarApplication: Application() {
    private val modules = listOf(calendarModule)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CalendarApplication)
            modules(modules)
        }
    }
}