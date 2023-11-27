package com.indieme.kalorie.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity("calendar_date")
class DateEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo("time_in_seconds") val timeInSeconds: Long,

    @ColumnInfo("selected") var selected: Boolean
)