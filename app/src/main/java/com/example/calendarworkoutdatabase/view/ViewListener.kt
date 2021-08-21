package com.example.calendarworkoutdatabase.view

import com.example.calendarworkoutdatabase.viewmodel.ViewState

interface ViewListener {
    fun setNewViewState(viewState: ViewState)
}