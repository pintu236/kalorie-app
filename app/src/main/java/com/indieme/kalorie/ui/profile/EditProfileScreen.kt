package com.indieme.kalorie.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.data.model.ActivityLevel
import com.indieme.kalorie.data.model.BottomSheetProfileState
import com.indieme.kalorie.data.model.GoalType
import com.indieme.kalorie.data.model.HeightUnit
import com.indieme.kalorie.data.model.INumberPicker
import com.indieme.kalorie.data.model.InputDialogProfileState
import com.indieme.kalorie.data.model.StringPickerDTO
import com.indieme.kalorie.data.model.WeightUnit
import com.indieme.kalorie.ui.base.DialogWithTextField
import com.indieme.kalorie.ui.base.KalorieDatePickerDialog
import com.indieme.kalorie.ui.base.NumberPickerBottomSheet
import com.indieme.kalorie.ui.theme.fontSizeLarge
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.paddingMedium
import com.indieme.kalorie.ui.theme.paddingSmall
import com.indieme.kalorie.ui.theme.secondaryTextColor
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.TimingUtils
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    parentNavController: NavController, profileViewModel: EditProfileViewModel = viewModel()
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    val usernameValue = remember { mutableStateOf("") }
    val emailValue = remember { mutableStateOf("") }
    var bottomSheetData by remember {
        mutableStateOf(BottomSheetProfileState(0, 0, listOf()))
    }
    val currentWeightValue = remember {
        mutableStateOf("")
    }
    val currentHeightValue = remember {
        mutableStateOf("")
    }
    val genderValue = remember {
        mutableStateOf("")
    }

    val goalWeightValue = remember {
        mutableStateOf("")
    }
    val ageValue = remember {
        mutableStateOf("")
    }
    val targetDateValue = remember {
        mutableStateOf("")
    }

    val goalTypeValue = remember {
        mutableStateOf("")
    }

    val activityLevelValue = remember {
        mutableStateOf("")
    }

    val weightUnitValue = remember {
        mutableStateOf("")
    }

    val heightUnitValue = remember {
        mutableStateOf("")
    }


    val goalTypesValues = remember {
        mutableStateListOf<INumberPicker>()
    }

    val activityLevelValues = remember {
        mutableStateListOf<INumberPicker>()
    }

    val weightUnitValues = remember {
        mutableStateListOf<INumberPicker>()
    }

    val heightUnitValues = remember {
        mutableStateListOf<INumberPicker>()
    }

    val bottomSheetModalState = rememberModalBottomSheetState(confirmValueChange = {
        false
    });

    val inputDialog = remember { mutableStateOf(InputDialogProfileState(0, 0, 0)) }


    val dialogState = remember {
        mutableStateOf(false)
    }

    val bottomSheetState = remember {
        mutableStateOf(false)
    }

    val datePickerState = remember {
        mutableStateOf(false)
    }

    var loadingState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        profileViewModel.profileState.collect { uiState ->
            when (uiState) {
                ProfileUiState.Empty -> {
                    loadingState = false
                }

                is ProfileUiState.Error -> {
                    AppUtil.showMessage(context, uiState.exception)
                    loadingState = false
                }

                ProfileUiState.Loading -> {
                    loadingState = true

                }

                is ProfileUiState.Success -> {
                    loadingState = false
                    usernameValue.value = uiState.userDTO.username
                    emailValue.value = uiState.userDTO.email
                    currentWeightValue.value = uiState.userDTO.userProfile?.currentWeight ?: ""
                    genderValue.value = uiState.userDTO.userProfile?.gender ?: ""
                    ageValue.value = uiState.userDTO.userProfile?.age ?: ""
                    currentHeightValue.value = uiState.userDTO.userProfile?.currentHeight ?: ""
                    goalWeightValue.value = uiState.userDTO.userProfile?.targetWeight ?: ""

                    goalTypeValue.value =
                        GoalType.getDisplayName(uiState.userDTO.userProfile?.goalType) ?: ""
                    activityLevelValue.value =
                        ActivityLevel.getDisplayName(uiState.userDTO.userProfile?.activityLevel) ?: ""

                    weightUnitValue.value =
                        WeightUnit.fromName(uiState.userDTO.userProfile?.weightUnit) ?: ""
                    heightUnitValue.value =
                        HeightUnit.fromName(uiState.userDTO.userProfile?.heightUnit) ?: ""

                    targetDateValue.value =
                        uiState.userDTO.userProfile?.targetDate?.split(" ")?.first() ?: ""
                    //
                    goalTypesValues.clear()
                    goalTypesValues.addAll(uiState.userDTO.goalTypes.map {
                        StringPickerDTO(
                            GoalType.valueOf(it).displayName
                        )
                    })
                    //
                    activityLevelValues.clear()
                    activityLevelValues.addAll(uiState.userDTO.activityLevels.map {
                        StringPickerDTO(
                            ActivityLevel.valueOf(it).displayName
                        )
                    })
                    //
                    weightUnitValues.clear()
                    weightUnitValues.addAll(uiState.userDTO.weightUnits.map {
                        StringPickerDTO(
                            WeightUnit.valueOf(it).displayName
                        )
                    })
                    //
                    heightUnitValues.clear()
                    heightUnitValues.addAll(uiState.userDTO.heightUnits.map {
                        StringPickerDTO(
                            HeightUnit.valueOf(it).displayName
                        )
                    })

                }
            }

        }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        stringResource(id = R.string.label_edit_profile),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    if (loadingState) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }

                }

            }, navigationIcon = {
                Icon(painterResource(id = R.drawable.ic_back), "", Modifier.clickable {
                    parentNavController.navigateUp()
                })
            }, modifier = Modifier.padding(horizontal = paddingMedium))
        },
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(Modifier.padding(paddingLarge)) {
                Text(
                    stringResource(id = R.string.label_user_name),
                    style = MaterialTheme.typography.labelSmall
                )
                Box(Modifier.height(paddingSmall))
                OutlinedTextField(
                    usernameValue.value, {
                        usernameValue.value = it
                    }, keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    ), modifier = Modifier.fillMaxWidth(), trailingIcon = {
                        Icon(Icons.Outlined.Person, "person")
                    }, enabled = false
                )
                Box(Modifier.height(paddingLarge))
                Text(
                    stringResource(id = R.string.label_email),
                    style = MaterialTheme.typography.labelSmall
                )
                Box(Modifier.height(paddingSmall))
                OutlinedTextField(
                    emailValue.value, {
                        emailValue.value = it
                    }, keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                    ), modifier = Modifier.fillMaxWidth(), trailingIcon = {
                        Icon(Icons.Outlined.Email, "person")
                    }, enabled = false
                )
                Box(Modifier.height(paddingLarge))
                Text(
                    stringResource(id = R.string.label_about_you),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = fontSizeLarge)
                )
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_gender),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(genderValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            bottomSheetData = BottomSheetProfileState(
                                1, R.string.label_gender, AppUtil.getGenderList()
                            )
                            bottomSheetState.value = true
                        })
                }
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_age),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(ageValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            inputDialog.value = InputDialogProfileState(
                                1, R.string.label_age, R.string.hint_weight_input
                            )
                            dialogState.value = true
                        })
                }
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_current_weight),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        stringResource(
                            id = R.string.value_with_unit,
                            currentWeightValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                            weightUnitValue.value
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            inputDialog.value = InputDialogProfileState(
                                2, R.string.label_current_weight, R.string.hint_weight_input
                            )
                            dialogState.value = true

                        })
                }
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_height),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        stringResource(
                            id = R.string.value_with_unit,
                            currentHeightValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                            heightUnitValue.value
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            inputDialog.value = InputDialogProfileState(
                                3, R.string.label_height, R.string.hint_weight_input
                            )
                            dialogState.value = true
                        })
                }
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_activity_level),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(activityLevelValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            bottomSheetData = BottomSheetProfileState(
                                3, R.string.label_activity_level, activityLevelValues
                            )
                            bottomSheetState.value = true
                        })
                }
                Box(Modifier.height(paddingLarge))
                Text(
                    stringResource(id = R.string.label_goals),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = fontSizeLarge)
                )
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_goal_type),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(goalTypeValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            bottomSheetData = BottomSheetProfileState(
                                2, R.string.label_goal_type, goalTypesValues
                            )
                            bottomSheetState.value = true
                        })
                }
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_goal_weight),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        stringResource(
                            id = R.string.value_with_unit,
                            goalWeightValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                            weightUnitValue.value
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            inputDialog.value = InputDialogProfileState(
                                4, R.string.label_goal_weight, R.string.hint_weight_input
                            )
                            dialogState.value = true
                        })
                }
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_target_date),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(targetDateValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            datePickerState.value = true
                        })
                }
                Box(Modifier.height(paddingLarge))
                Text(
                    stringResource(id = R.string.label_units),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = fontSizeLarge)
                )
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_weight_unit),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(weightUnitValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            bottomSheetData = BottomSheetProfileState(
                                4, R.string.label_weight_unit, weightUnitValues
                            )
                            bottomSheetState.value = true
                        })
                }
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_height_unit),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(heightUnitValue.value.ifEmpty { stringResource(id = R.string.label_na) },
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            bottomSheetData = BottomSheetProfileState(
                                5, R.string.label_height_unit, heightUnitValues
                            )
                            bottomSheetState.value = true
                        })
                }


            }

            if (dialogState.value) {
                DialogWithTextField(
                    onDismissRequest = {
                        dialogState.value = false
                    },
                    onConfirmation = { item ->
                        when (inputDialog.value.type) {
                            1 -> {
                                profileViewModel.updateAge(item)
                            }

                            2 -> {
                                profileViewModel.updateCurrentWeight(item)
                            }

                            3 -> {
                                profileViewModel.updateCurrentHeight(item)
                            }

                            4 -> {
                                profileViewModel.updateGoalWeight(item)
                            }

                        }
                        profileViewModel.saveProfile()
                        dialogState.value = false

                    },
                    dialogTitle = stringResource(id = inputDialog.value.title),
                    icon = Icons.Default.Info,
                    hint = stringResource(id = inputDialog.value.hint)
                )
            }

            if (bottomSheetState.value) {

                ModalBottomSheet(onDismissRequest = { bottomSheetState.value = false },
                    sheetState = bottomSheetModalState,
                    content = {
                        NumberPickerBottomSheet(title = stringResource(id = bottomSheetData.hint),
                            bottomSheetData.data,
                            onItemSelect = { item ->
                                when (bottomSheetData.type) {
                                    1 -> {
                                        profileViewModel.updateGender(item)
                                    }

                                    2 -> {
                                        profileViewModel.updateGoalType(
                                            GoalType.fromDisplayName(
                                                item
                                            ).name
                                        )
                                    }

                                    3 -> {
                                        profileViewModel.updateActivityLevel(
                                            ActivityLevel.fromDisplayName(
                                                item
                                            ).name
                                        )
                                    }

                                    4 -> {
                                        profileViewModel.updateWeightUnit(item)
                                    }

                                    5 -> {
                                        profileViewModel.updateHeightUnit(item)
                                    }
                                }
                                bottomSheetState.value = false
                                profileViewModel.saveProfile()
                            }) {
                            coroutineScope.launch {
                                bottomSheetModalState.hide()
                                bottomSheetState.value = false

                            }

                        }
                    })
            }

            if (datePickerState.value) {
                KalorieDatePickerDialog(
                    onDateSelected = {
                        profileViewModel.updateTargetDate(it)
                        profileViewModel.saveProfile()
                    },
                    onDismiss = { datePickerState.value = false },
                    dateFormat = TimingUtils.TimeFormats.CUSTOM_YYYY_MM_DD.timeFormat
                )
            }


        }


    }
}