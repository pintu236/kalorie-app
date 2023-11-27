package com.indieme.kalorie.ui.home

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.Screen
import com.indieme.kalorie.data.manager.LocalPreferenceManager
import com.indieme.kalorie.ui.base.PieChart
import com.indieme.kalorie.ui.base.ViewModelFactoryWithApplication
import com.indieme.kalorie.ui.theme.defaultCardElevation
import com.indieme.kalorie.ui.theme.minPieChartBarWidth
import com.indieme.kalorie.ui.theme.minPieChartSize
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.ui.theme.primaryTextWhiteColor
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.afternoonGreeting
import com.indieme.kalorie.utils.eveningGreeting
import com.indieme.kalorie.utils.morningGreeting
import com.indieme.kalorie.utils.nightGreeting
import kotlinx.coroutines.flow.collect
import java.time.LocalTime


@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactoryWithApplication(LocalContext.current.applicationContext as Application))
) {
    val context = LocalContext.current;
    val userName = remember {
        mutableStateOf("")
    }


    var showBMRInfo by remember {
        mutableStateOf(false)
    }

    var showBMIInfo by remember {
        mutableStateOf(false)
    }

    var showDailyCalorieInfo by remember {
        mutableStateOf(false)
    }
    var showTDEEInfo by remember {
        mutableStateOf(false)
    }

    val calculatedBMR = homeViewModel.bmrFlow.collectAsState("")
    val calculatedTDEE = homeViewModel.TDEEFlow.collectAsState("")
    val calculatedDailyCalorie = homeViewModel.dailyCalorieFlow.collectAsState("0")
    val calculatedConsumedCalorie = homeViewModel.consumedCalorieFlow.collectAsState("0")
    val calculatedBMI = homeViewModel.bmiFlow.collectAsState("0")
    val alertMessage = homeViewModel.message.collectAsState()



    LaunchedEffect(Unit) {
        homeViewModel.userState.collect { uiState ->
            if (uiState is HomeUiState.Success) {
                userName.value = uiState.userDTO.username
            }

        }
    }

    LaunchedEffect(alertMessage) {
        homeViewModel.consumedMealsFlow.collect { food ->
            val totalCalorie = if (food.isNotEmpty()) {
                food.map { it.calorie }
                    .reduce { acc, value ->
                        acc?.plus(value!!)
                    }

            } else {
                0
            }
            LocalPreferenceManager.saveDailyConsumedCalorie(context, totalCalorie.toString())

        }
    }


    val currentTime = LocalTime.now()


    LazyColumn(modifier = Modifier.padding(paddingLarge)) {
        item {
            val greeting = when {
                currentTime.isBefore(LocalTime.of(12, 0)) -> morningGreeting
                currentTime.isBefore(LocalTime.of(17, 0)) -> afternoonGreeting
                currentTime.isBefore(LocalTime.of(20, 0)) -> eveningGreeting
                else -> nightGreeting
            }

            Text(text = "$greeting ${userName.value}")
            Box(Modifier.height(paddingLarge))
            Card(

                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultCardElevation)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(paddingLarge)
                ) {
                    Text(
                        stringResource(id = R.string.label_add_today_intake),
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium
                    )
                    Box(Modifier.padding(8.dp))
                    ElevatedButton(
                        onClick = {
                            navController.navigate(Screen.TrackFoodCalorie.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        modifier = Modifier.weight(0.5f)

                    ) {
                        Text(
                            stringResource(id = R.string.action_track),
                            style = MaterialTheme.typography.labelMedium
                        )

                    }
                }
            }

            Box(Modifier.height(paddingLarge))
            Row() {
                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    elevation = CardDefaults.cardElevation(defaultCardElevation)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = paddingLarge)
                            .padding(bottom = paddingLarge)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                stringResource(id = R.string.label_your_bmr),
                                textAlign = TextAlign.Left,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelMedium.copy(color = primaryTextWhiteColor)
                            )

                            IconButton(
                                onClick = { showBMRInfo = true },
                                modifier = Modifier.defaultMinSize(minHeight = 16.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "info"
                                )
                            }
                        }
                        Text(
                            stringResource(
                                id = R.string.value_with_unit,
                                calculatedBMR.value.ifEmpty { stringResource(id = R.string.label_no_calculated) },
                                stringResource(id = R.string.label_calorie)
                            ),
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                        )
                    }

                }

                Box(Modifier.width(paddingLarge))
                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    elevation = CardDefaults.cardElevation(defaultCardElevation)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = paddingLarge)
                            .padding(bottom = paddingLarge)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                stringResource(id = R.string.label_your_tdee),
                                textAlign = TextAlign.Left,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelMedium.copy(color = primaryTextWhiteColor)
                            )

                            IconButton(
                                onClick = { showTDEEInfo = true },
                                modifier = Modifier.defaultMinSize(minHeight = 16.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "info"
                                )
                            }
                        }
                        Text(
                            stringResource(
                                id = R.string.value_with_unit,
                                calculatedTDEE.value.ifEmpty { stringResource(id = R.string.label_no_calculated) },
                                stringResource(id = R.string.label_calorie)
                            ),
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                        )
                    }

                }
            }
            Box(Modifier.height(paddingLarge))
            Card(

                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultCardElevation)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = paddingLarge)
                        .padding(bottom = paddingLarge)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            stringResource(id = R.string.label_your_bmi),
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelMedium.copy(color = primaryTextWhiteColor)
                        )

                        IconButton(
                            onClick = { showBMIInfo = true },
                            modifier = Modifier.defaultMinSize(minHeight = 16.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "info"
                            )
                        }
                    }
                    Text(
                        if (calculatedBMI.value.isEmpty()) stringResource(id = R.string.label_no_calculated) else
                            stringResource(
                                id = R.string.label_with_paranthesis, AppUtil.interpretBMI(
                                    calculatedBMI.value.toDouble()
                                ), calculatedBMI.value.toDouble()
                            ),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                    )
                }

            }

            Box(Modifier.height(paddingLarge))
            Card(

                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultCardElevation)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = paddingLarge)
                        .padding(bottom = paddingLarge)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            stringResource(id = R.string.label_your_daily_calorie_intake),
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelMedium.copy(color = primaryTextWhiteColor)
                        )

                        IconButton(
                            onClick = { showDailyCalorieInfo = true },
                            modifier = Modifier.defaultMinSize(minHeight = 16.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "info"
                            )
                        }
                    }

                    Box(Modifier.height(paddingLarge))
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Preview with sample data
                        PieChart(
                            data = mapOf(
                                Pair(
                                    stringResource(id = R.string.label_consumed_calorie),
                                    calculatedConsumedCalorie.value.toDouble()
                                ),
                                Pair(
                                    stringResource(id = R.string.label_remaining_calorie),
                                    calculatedDailyCalorie.value.toDouble() - calculatedConsumedCalorie.value.toDouble()
                                ),
                            ), radiusOuter = minPieChartSize,
                            chartBarWidth = minPieChartBarWidth
                        )

                    }
                }

            }
        }
    }

    if (showTDEEInfo) {
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(stringResource(id = R.string.label_info))
            },
            text = {
                Text(
                    stringResource(id = R.string.hint_tdee_info),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTDEEInfo = false
                    }
                ) {
                    Text(
                        stringResource(id = R.string.action_ok),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }

    if (showBMRInfo) {
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(stringResource(id = R.string.label_info))
            },
            text = {
                Text(
                    stringResource(id = R.string.hint_bmr_info),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBMRInfo = false
                    }
                ) {
                    Text(
                        stringResource(id = R.string.action_ok),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }

    if (showDailyCalorieInfo) {
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(stringResource(id = R.string.label_info))
            },
            text = {
                Text(
                    stringResource(id = R.string.hint_daily_calorie_intake),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDailyCalorieInfo = false
                    }
                ) {
                    Text(
                        stringResource(id = R.string.action_ok),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }

    if (showBMIInfo) {
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(stringResource(id = R.string.label_info))
            },
            text = {
                Text(
                    stringResource(id = R.string.hint_bmi_info),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBMIInfo = false
                    }
                ) {
                    Text(
                        stringResource(id = R.string.action_ok),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }
}


