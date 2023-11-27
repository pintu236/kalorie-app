package com.indieme.kalorie.ui.trackfood

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.Screen
import com.indieme.kalorie.data.model.FoodDTO
import com.indieme.kalorie.ui.theme.defaultCornerSize
import com.indieme.kalorie.ui.theme.fieldBackGroundColor
import com.indieme.kalorie.ui.theme.fontSizeSmall
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.paddingMedium
import com.indieme.kalorie.ui.theme.paddingSmall
import com.indieme.kalorie.ui.theme.paddingSmallMedium
import com.indieme.kalorie.ui.theme.primaryTextColor
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.MEAL_BREAKFAST
import com.indieme.kalorie.utils.MEAL_DINNER
import com.indieme.kalorie.utils.MEAL_LUNCH
import com.indieme.kalorie.utils.MEAL_SNACKS
import com.indieme.kalorie.utils.TYPE_CALORIE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchAndAddFoodScreen(
    navController: NavController,
    mealId: Int,
    selectedDate: Long,
    searchAddFoodViewModel: SearchAddFoodViewModel = viewModel()
) {

    Log.d("Meal Id: ", mealId.toString())
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current


    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var selectedMeal by remember {
        mutableStateOf(mealId)
    }

    val searchValue = remember { mutableStateOf("") }
    val loadingState = remember {
        mutableStateOf(false)
    }
    val foodItems = remember {
        mutableStateListOf<FoodDTO>()
    }

    val openBottomSheet = rememberSaveable { mutableStateOf(-1) }
    val skipPartiallyExpanded = remember { mutableStateOf(false) }


    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded.value,
    )

    LaunchedEffect(Unit) {
        searchAddFoodViewModel.searchFoodState.collect() { uiState ->
            when (uiState) {
                SearchAddFoodViewModelUiState.Empty -> {
                    loadingState.value = false
                }

                is SearchAddFoodViewModelUiState.Error -> {
                    AppUtil.showMessage(context, uiState.exception)
                    loadingState.value = false
                }

                SearchAddFoodViewModelUiState.Loading -> {
                    loadingState.value = true
                }

                is SearchAddFoodViewModelUiState.Success -> {
                    loadingState.value = false
                    foodItems.clear()
                    foodItems.addAll(uiState.foodList)
                }
            }

        }
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopCenter)
            ) {
                Row(Modifier.clickable {
                    expanded = !expanded
                }, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = AppUtil.getMealNameById(selectedMeal))
                    Icon(Icons.Default.KeyboardArrowDown, "")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(id = R.string.label_breakfast),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        onClick = {
                            expanded = false
                            selectedMeal = MEAL_BREAKFAST
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(id = R.string.label_lunch),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        onClick = {
                            expanded = false
                            selectedMeal = MEAL_LUNCH
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(id = R.string.label_dinner),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        onClick = {
                            expanded = false
                            selectedMeal = MEAL_DINNER
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(id = R.string.label_snacks),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        onClick = {
                            expanded = false
                            selectedMeal = MEAL_SNACKS
                        }
                    )
                }
            }
        }, navigationIcon = {
            Icon(painterResource(id = R.drawable.ic_back), "")
        }, modifier = Modifier.padding(paddingLarge))
    }) { innerPadding ->

        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .padding(horizontal = paddingLarge)
                    .align(Alignment.TopStart)
            ) {
                OutlinedTextField(
                    searchValue.value,
                    {
                        searchValue.value = it
                    },
                    placeholder = { Text(text = stringResource(id = R.string.hint_search_food)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardController?.hide()
                        searchAddFoodViewModel.searchFood(searchValue.value.trim())
                    }),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(defaultCornerSize),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = fieldBackGroundColor,
                        focusedContainerColor = fieldBackGroundColor,
                        focusedIndicatorColor = fieldBackGroundColor,
                        unfocusedIndicatorColor = fieldBackGroundColor,
                        focusedTextColor = primaryTextColor
                    ),
                    leadingIcon = {
                        Icon(Icons.Outlined.Search, "Search")
                    },

                    )
                LazyColumn() {
                    items(foodItems.size) { index ->
                        SearchItem(
                            navController, Modifier
                                .fillMaxSize()
                                .padding(vertical = paddingMedium)
                                .clickable {
                                    openBottomSheet.value = index

                                }, foodItems[index]
                        )

                    }
                }
            }

            if (loadingState.value) {

                CircularProgressIndicator(color = Color.White)
            }


        }

    }

    if (openBottomSheet.value != -1) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = -1 },
            sheetState = bottomSheetState
        ) {

            if (openBottomSheet.value != -1 && foodItems.isNotEmpty()) {
                FoodInfoScreen(
                    selectedMeal,
                    selectedDate,
                    foodItems[openBottomSheet.value],
                    onClose = {

                        coroutineScope.launch {
                            openBottomSheet.value = -1
                            bottomSheetState.hide()
                        }

                    },
                    foodInfoViewModel = viewModel()
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItem(navController: NavController, modifier: Modifier, item: FoodDTO) {


    val nutrient = item.nutrients.first();
    val cal = nutrient.foodNutrients.find {
        it.nutrientId == TYPE_CALORIE
    }?.value

    Card(modifier) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingMedium)
        ) {
            Text(item.name.toString(), style = MaterialTheme.typography.labelMedium)
            Text(
                item.description.toString(),
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = fontSizeSmall)
            )
            Text(
                stringResource(
                    id = R.string.label_calorie_info,
                    cal.toString(),
                    nutrient.servingSizeName.toString()
                ), style = MaterialTheme.typography.labelSmall.copy(fontSize = fontSizeSmall)
            )
        }
    }
}
