package com.indieme.kalorie.ui.trackfood

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indieme.kalorie.R
import com.indieme.kalorie.data.model.FoodDTO
import com.indieme.kalorie.data.model.NutrientsDTO
import com.indieme.kalorie.ui.theme.iconColor
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.paddingMedium
import com.indieme.kalorie.ui.theme.paddingSmallMedium
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.K_ADDED_ON
import com.indieme.kalorie.utils.K_FOOD_ID
import com.indieme.kalorie.utils.K_MEAL_ID
import com.indieme.kalorie.utils.K_NO_OF_SERVING
import com.indieme.kalorie.utils.K_SERVING_SIZE_ID
import com.indieme.kalorie.utils.TYPE_CALORIE
import com.indieme.kalorie.utils.TYPE_CARBS
import com.indieme.kalorie.utils.TYPE_FAT
import com.indieme.kalorie.utils.TYPE_PROTIEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInfoScreen(
    selectedMeal: Int,
    selectedDate: Long,
    foodDTO: FoodDTO?,
    onClose: () -> Unit,
    foodInfoViewModel: FoodInfoViewModel
) {

    val context = LocalContext.current

    val loadingState = remember {
        mutableStateOf(false)
    }
    val nutrientsItems = remember {
        mutableStateListOf<NutrientsDTO>()
    }

    val servingSizeState = remember {
        mutableStateOf(0)
    }

    val noOfServingState = remember {
        mutableStateOf("1")
    }

    val lifeCycleOwner = LocalLifecycleOwner.current

    val uiState =
        foodInfoViewModel.foodInfoState.collectAsStateWithLifecycle(lifecycle = lifeCycleOwner.lifecycle).value
    when (uiState) {
        FoodInfoUiState.Empty -> {
            loadingState.value = false
        }

        is FoodInfoUiState.Error -> {
            loadingState.value = false
            AppUtil.showMessage(context, uiState.exception)
        }

        FoodInfoUiState.Loading -> {
            loadingState.value = true
        }

        is FoodInfoUiState.Success -> {
            loadingState.value = false
            AppUtil.showMessage(context, uiState.message)
            onClose()

        }
    }




    Scaffold { innerPadding ->
        Column(Modifier.padding(horizontal = paddingLarge)) {

            Box(Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_meal), contentDescription = "",
                    Modifier.align(Alignment.Center), colorFilter = ColorFilter.tint(iconColor)
                )
            }
            Box(Modifier.height(paddingLarge))
            NutrientInfoCard(
                foodDTO = foodDTO,
                servingSize = servingSizeState.value,
                noOfServing = if (noOfServingState.value.isEmpty()) 1 else noOfServingState.value.toInt()
            )
            Box(Modifier.height(paddingLarge))
            Text(stringResource(id = R.string.header_no_of_serving))
            OutlinedTextField(
                noOfServingState.value,
                {
                    noOfServingState.value = it.filter { f ->
                        f.isDigit()
                    }.take(2)

                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ), modifier = Modifier
                    .width(56.dp)
                    .heightIn(40.dp),
                shape = RoundedCornerShape(12.dp), textStyle = MaterialTheme.typography.labelSmall
            )
            Box(Modifier.height(paddingLarge))
            Text(stringResource(id = R.string.header_serving_size))
            LazyRow {
                items(foodDTO?.nutrients?.size ?: 0) { index ->
                    InputChip(selected = servingSizeState.value == index, onClick = {
                        servingSizeState.value = index
                    }, label = {
                        Text(
                            foodDTO?.nutrients?.get(index)?.servingSizeName.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }, modifier = Modifier.padding(horizontal = paddingSmallMedium))
                }

            }

            Box(Modifier.height(paddingLarge))
            Text(stringResource(id = R.string.header_name))
            Text(foodDTO?.name.toString())

            Box(Modifier.height(paddingLarge))
            Text(stringResource(id = R.string.header_details))
            Text(foodDTO?.description.toString())
            Box(Modifier.height(paddingLarge))
            ElevatedButton(
                onClick = {
                    if (noOfServingState.value.isEmpty()) {
                        AppUtil.showMessage(context, "Invalid no of serving")
                    }
                    val hashMap = hashMapOf<String, Any?>()

                    hashMap[K_SERVING_SIZE_ID] =
                        foodDTO?.nutrients?.get(servingSizeState.value)?.servingSizeId

                    hashMap[K_MEAL_ID] = selectedMeal
                    hashMap[K_FOOD_ID] = foodDTO?.id
                    hashMap[K_NO_OF_SERVING] = noOfServingState.value
                    hashMap[K_ADDED_ON] = selectedDate

                    foodInfoViewModel.addFoodTracking(hashMap)

                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (loadingState.value) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(
                        stringResource(id = R.string.action_add_meal),
                        style = MaterialTheme.typography.labelLarge
                    )
                }


            }
        }
    }
}

@Composable
fun NutrientInfoCard(foodDTO: FoodDTO?, servingSize: Int, noOfServing: Int = 1) {

    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(paddingMedium)
        ) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(id = R.string.label_protien))
                Text(
                    stringResource(
                        id = R.string.label_value_in_gm,
                        (AppUtil.findNutrientById(
                            TYPE_PROTIEN,
                            foodDTO?.nutrients?.get(servingSize)?.foodNutrients
                        )?.value?.times(noOfServing)
                                ).toString()
                    )
                )
            }
            Column(Modifier.weight(1f)) {
                Text(stringResource(id = R.string.label_fat))
                Text(
                    stringResource(
                        id = R.string.label_value_in_gm, AppUtil.findNutrientById(
                            TYPE_FAT,
                            foodDTO?.nutrients?.get(servingSize)?.foodNutrients
                        )?.value?.times(noOfServing).toString()
                    )
                )
            }
            Column(Modifier.weight(1f)) {
                Text(stringResource(id = R.string.label_carbs))
                Text(
                    stringResource(
                        id = R.string.label_value_in_gm, AppUtil.findNutrientById(
                            TYPE_CARBS,
                            foodDTO?.nutrients?.get(servingSize)?.foodNutrients
                        )?.value?.times(noOfServing).toString()
                    )
                )
            }
            Column(Modifier.weight(1f)) {
                Text(stringResource(id = R.string.label_calories))
                Text(
                    stringResource(
                        id = R.string.label_value_in_gm, AppUtil.findNutrientById(
                            TYPE_CALORIE,
                            foodDTO?.nutrients?.get(servingSize)?.foodNutrients
                        )?.value?.times(noOfServing).toString()
                    )
                )
            }
        }
    }
}