package com.indieme.kalorie.data.model

import com.indieme.kalorie.data.local.entity.DateEntity
import java.util.Date

data class TrackFoodDTO(val selectedIndex: Int, val listOfDates: MutableList<DateEntity>)