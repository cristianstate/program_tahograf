package com.fitness.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucker_logs")
data class TruckerLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val startTime: String,
    val endTime: String,
    val drivingTime: String,
    val restDuration: String
)
