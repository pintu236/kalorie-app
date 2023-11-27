package com.indieme.kalorie.ui.trackfood

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.Screen
import com.indieme.kalorie.data.local.entity.DateEntity
import com.indieme.kalorie.data.local.entity.MealWithTrackMealDTO
import com.indieme.kalorie.data.local.entity.TrackMealEntity
import com.indieme.kalorie.data.model.DateDTO
import com.indieme.kalorie.data.model.FoodDTO
import com.indieme.kalorie.data.model.TrackMealDTO
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.paddingMedium
import com.indieme.kalorie.ui.theme.paddingSmall
import com.indieme.kalorie.ui.theme.paddingSmallMedium
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.MEAL_BREAKFAST
import com.indieme.kalorie.utils.MEAL_DINNER
import com.indieme.kalorie.utils.MEAL_LUNCH
import com.indieme.kalorie.utils.MEAL_SNACKS
import com.indieme.kalorie.utils.TimingUtils


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackFoodCalorieScreen(
    parentNavController: NavController,
    trackFoodCalorieViewModel: TrackFoodCalorieViewModel = viewModel()
) {

    val context = LocalContext.current;

    val list = remember {
        mutableStateListOf<DateEntity>()
    }
    var selectedDate = remember {
        mutableStateOf(0L)
    }

    val state = rememberLazyListState()
    // If you'd like to customize either the snap behavior or the layout provider
    val snappingLayout = remember(state) {
        SnapLayoutInfoProvider(state)
    }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)


    val listOfMeals = remember {
        mutableStateListOf<MealWithTrackMealDTO>()
    }

    val breakfastMeal = remember {
        mutableStateOf(MealWithTrackMealDTO())
    }
    val lunchMeal = remember {
        mutableStateOf(MealWithTrackMealDTO())
    }
    val dinnerMeal = remember {
        mutableStateOf(MealWithTrackMealDTO())
    }
    val snacksMeal = remember {
        mutableStateOf(MealWithTrackMealDTO())
    }




    LaunchedEffect(Unit) {
        trackFoodCalorieViewModel.trackFoodState.collect {
            if (it is TrackFoodUiState.Success) {
                list.clear()
                list.addAll(it.trackFoodDTO.listOfDates)


                state.scrollToItem(it.trackFoodDTO.selectedIndex)
            }
        }

    }

    LaunchedEffect(Unit) {
        trackFoodCalorieViewModel.trackMealState.collect {

            when (it) {
                TrackMealUiState.Empty -> {

                }

                is TrackMealUiState.Error -> {
                    AppUtil.showMessage(context, it.exception)
                }

                TrackMealUiState.Loading -> {

                }

                is TrackMealUiState.Success -> {
                    listOfMeals.clear()
                    listOfMeals.addAll(it.mealDTO)
                    //
                    breakfastMeal.value = getMealById(MEAL_BREAKFAST, listOfMeals)
                    lunchMeal.value = getMealById(MEAL_LUNCH, listOfMeals)
                    dinnerMeal.value = getMealById(MEAL_DINNER, listOfMeals)
                    snacksMeal.value = getMealById(MEAL_SNACKS, listOfMeals)
                }
            }
        }

    }

    LaunchedEffect(Unit) {
        trackFoodCalorieViewModel.dateChangeState.collect {
            if (it is DateChangeUiState.Success) {
                selectedDate.value = it.dateDTO.timeInSeconds
                trackFoodCalorieViewModel.getMeals(it.dateDTO.timeInSeconds)
            }
        }
    }



    Column(
        modifier = Modifier.padding(horizontal = paddingLarge)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Box {

                Image(painter = painterResource(id = R.drawable.fi_chevron_left),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {

                        })

                Image(painter = painterResource(id = R.drawable.fi_chevron_right),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {

                        })
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            paddingLarge
                        ), state = state, flingBehavior = flingBehavior
                ) {
                    items(list) { item ->
                        DateItem(
                            Modifier
                                .fillParentMaxWidth()
                                .padding(paddingSmallMedium),
                            state,
                            list.indexOf(item),
                            item,
                            trackFoodCalorieViewModel
                        )

                    }
                }
            }

        }
        Box(
            modifier = Modifier.height(paddingLarge)
        )
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {

            Card(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(paddingLarge)) {
                    Column(Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(stringResource(id = R.string.label_breakfast))
                            Text(
                                stringResource(
                                    id = R.string.label_total_calorie,
                                    breakfastMeal.value.mealEntity?.totalCalorie ?: 0.0
                                )

                            )
                        }
                        Box(
                            modifier = Modifier.height(paddingMedium)
                        )
                        breakfastMeal.value.trackMealEntityList.forEach { foodItem ->
                            FoodItem(foodItem) {
                                trackFoodCalorieViewModel.delete(foodItem,selectedDate.value)
                            }
                        }
                        Box(
                            modifier = Modifier.height(paddingMedium)
                        )
                        ElevatedButton(
                            onClick = {
                                moveToSearchAndAddFoodScreen(
                                    parentNavController,
                                    MEAL_BREAKFAST,
                                    selectedDate.value
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                stringResource(id = R.string.action_add_more_meal),
                                style = MaterialTheme.typography.labelLarge
                            )

                        }
                    }


                }
            }
            Box(
                modifier = Modifier.height(paddingLarge)
            )
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(paddingLarge)) {
                    Column(Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(stringResource(id = R.string.label_lunch))
                            Text(
                                stringResource(
                                    id = R.string.label_total_calorie,
                                    lunchMeal.value.mealEntity?.totalCalorie ?: 0.0
                                )
                            )
                        }
                        Box(
                            modifier = Modifier.height(paddingMedium)
                        )
                        lunchMeal.value.trackMealEntityList.forEach { foodItem ->
                            FoodItem(foodItem) {
                                trackFoodCalorieViewModel.delete(foodItem,selectedDate.value)
                            }
                        }
                        Box(
                            modifier = Modifier.height(paddingMedium)
                        )
                        ElevatedButton(
                            onClick = {
                                moveToSearchAndAddFoodScreen(
                                    parentNavController,
                                    MEAL_LUNCH,
                                    selectedDate.value
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                stringResource(id = R.string.action_add_more_meal),
                                style = MaterialTheme.typography.labelLarge
                            )

                        }
                    }


                }
            }
            Box(
                modifier = Modifier.height(paddingLarge)
            )
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(paddingLarge)) {
                    Column(Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(stringResource(id = R.string.label_dinner))
                            Text(
                                stringResource(
                                    id = R.string.label_total_calorie,
                                    dinnerMeal.value.mealEntity?.totalCalorie ?: 0.0
                                )
                            )
                        }
                        Box(
                            modifier = Modifier.height(paddingMedium)
                        )
                        dinnerMeal.value.trackMealEntityList.forEach { foodItem ->
                            FoodItem(foodItem) {
                                trackFoodCalorieViewModel.delete(foodItem,selectedDate.value)
                            }
                        }
                        Box(
                            modifier = Modifier.height(paddingMedium)
                        )
                        ElevatedButton(
                            onClick = {
                                moveToSearchAndAddFoodScreen(
                                    parentNavController, MEAL_DINNER, selectedDate.value
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                stringResource(id = R.string.action_add_more_meal),
                                style = MaterialTheme.typography.labelLarge
                            )

                        }
                    }


                }
            }
            Box(
                modifier = Modifier.height(paddingLarge)
            )
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(paddingLarge)) {
                    Column(Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(stringResource(id = R.string.label_snacks))
                            Text(
                                stringResource(
                                    id = R.string.label_total_calorie,
                                    snacksMeal.value.mealEntity?.totalCalorie ?: 0.0
                                )
                            )
                        }
                        Box(
                            modifier = Modifier.height(paddingMedium)
                        )
                        snacksMeal.value.trackMealEntityList.forEach { foodItem ->
                            FoodItem(foodItem) {
                                trackFoodCalorieViewModel.delete(foodItem,selectedDate.value)
                            }
                        }
                        Box(
                            modifier = Modifier.height(paddingMedium)
                        )
                        ElevatedButton(
                            onClick = {
                                moveToSearchAndAddFoodScreen(
                                    parentNavController, MEAL_SNACKS, selectedDate.value
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                stringResource(id = R.string.action_add_more_meal),
                                style = MaterialTheme.typography.labelLarge
                            )

                        }
                    }


                }
            }

        }


    }


}

fun getMealById(
    meal: Int,
    listOfMeals: SnapshotStateList<MealWithTrackMealDTO>
): MealWithTrackMealDTO {
    return listOfMeals.find {
        it.mealEntity?.mealId == meal
    } ?: MealWithTrackMealDTO()
}

fun moveToSearchAndAddFoodScreen(
    parentNavController: NavController,
    mealId: Int,
    selectedDate: Long
) {
    parentNavController.navigate("${Screen.SearchAndAddFood.route}?value=$mealId&date=$selectedDate")
}

@Composable
fun FoodItem(item: TrackMealEntity, onDeleteClick: () -> Unit) {
    var visible by remember { mutableStateOf(true) }


    AnimatedVisibility(visible = visible) {
        Column(Modifier.padding(bottom = paddingMedium)) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    item.foodName.toString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "${item.noOfServing?.times(item.calorie ?: 0f)}",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall,
                )

            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${item.noOfServing.toString()},${item.servingSize}",
                    style = MaterialTheme.typography.bodySmall,
                )
                IconButton(onClick = {
                    onDeleteClick()
                    visible = false
                }) {
                    Icon(Icons.Default.Delete, "")
                }
            }
            Box(Modifier.height(paddingMedium))
            Divider(
                Modifier
                    .fillMaxWidth(), color = Color.LightGray
            )

        }
    }


}


@Composable
fun DateItem(
    modifier: Modifier,
    state: LazyListState,
    index: Int,
    item: DateEntity,
    trackFoodCalorieViewModel: TrackFoodCalorieViewModel
) {


    val firstVisibleIndex = remember { derivedStateOf { state.firstVisibleItemIndex } }


    if (firstVisibleIndex.value == index) {
        trackFoodCalorieViewModel.onDateChange(item)
    }


    Text(
        TimingUtils.getTimeInString(
            item.timeInSeconds, TimingUtils.TimeFormats.DD_MMM_YYYY
        ),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
    )
}
