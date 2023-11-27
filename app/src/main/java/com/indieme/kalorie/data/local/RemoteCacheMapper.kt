package com.indieme.kalorie.data.local

import com.indieme.kalorie.data.local.entity.DateEntity
import com.indieme.kalorie.data.local.entity.MealEntity
import com.indieme.kalorie.data.local.entity.TrackMealEntity
import com.indieme.kalorie.data.model.DateDTO
import com.indieme.kalorie.data.model.MealDTO
import com.indieme.kalorie.data.model.TrackMealDTO


object RemoteCacheMapper {

    fun toMealEntityList(mealDTOList: MutableList<MealDTO>): MutableList<MealEntity> {
        return mealDTOList.map {
            toMealEntity(it)
        }.toMutableList()
    }

    private fun toMealEntity(mealDTO: MealDTO): MealEntity {
        return MealEntity(
            mealId = mealDTO.mealId,
            meal = mealDTO.meal,
            totalCalorie = mealDTO.totalCalorie,
            addedOn = DateConverter.toDate(mealDTO.addedOn)
        )
    }

    fun fromMealEntity(mealDTO: MealDTO): MealDTO {
        return MealDTO(
            mealId = mealDTO.mealId,
            meal = mealDTO.meal,
            totalCalorie = mealDTO.totalCalorie,
            addedOn = mealDTO.addedOn
        )
    }

    fun toTrackMealEntityList(mealDTOList: MutableList<MealDTO>): MutableList<TrackMealEntity> {
        val trackMealEntityList = mutableListOf<TrackMealEntity>()
        mealDTOList.forEach {
            trackMealEntityList.addAll(toTrackMealEntity(it.foodDTOList))
        }

        return trackMealEntityList;

    }

    private fun toTrackMealEntity(mealDTOList: MutableList<TrackMealDTO>): MutableList<TrackMealEntity> {
        return mealDTOList.map {
            toTrackMealEntity(it)
        }.toMutableList()
    }

    fun toTrackMealEntity(trackMealDTO: TrackMealDTO): TrackMealEntity {
        return TrackMealEntity(
            foodId = trackMealDTO.foodId,
            mealId = trackMealDTO.mealId,
            servingSizeId = trackMealDTO.servingSizeId,
            addedOn = DateConverter.toDate(trackMealDTO.addedOn),
            noOfServing = trackMealDTO.noOfServing,
            foodName = trackMealDTO.foodName,
            servingSize = trackMealDTO.servingSize,
            calorie = trackMealDTO.calorie
        )
    }

    fun toDateEntity(dateDTOList: MutableList<DateDTO>): List<DateEntity> {

        return dateDTOList.map {
            DateEntity(0, timeInSeconds = it.timeInSeconds, selected = it.selected != 0)
        }.toMutableList()

    }
}