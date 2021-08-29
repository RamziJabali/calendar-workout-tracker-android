package com.example.calendarworkoutdatabase.util

import java.util.Date

fun Date.isAfterToday(): Boolean = this.after(Date())
