package com.indieme.kalorie.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.data.model.InputDialogProfileState
import com.indieme.kalorie.ui.base.DialogWithTextField
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.paddingMedium
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.ui.theme.secondaryTextColor
import com.indieme.kalorie.utils.AppUtil


@Composable
fun WeightHeightOnBoardingScreen(parentNavController: NavController) {
    val dialogState = remember {
        mutableStateOf(false)
    }

    val inputDialog = remember { mutableStateOf(InputDialogProfileState(0, 0, 0)) }


    Scaffold() { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingLarge)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(id = R.string.label_weight_and_height),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    stringResource(id = R.string.label_gender_onboarding_info),
                    style = MaterialTheme.typography.labelSmall, color = Color.LightGray,
                )
            }
            Box(Modifier.height(paddingLarge))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {

                Row() {
                    Text(
                        stringResource(id = R.string.label_current_weight),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "?",
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            inputDialog.value = InputDialogProfileState(
                                1, R.string.label_current_weight, R.string.hint_weight_input
                            )
                            dialogState.value = true
                        })
                    Box(Modifier.width(paddingLarge))
                    Text(
                        "?",
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {

                        })
                }
                Box(Modifier.height(paddingMedium))
                Row() {
                    Text(
                        stringResource(id = R.string.label_height),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "?",
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {

                        })
                    Box(Modifier.width(paddingLarge))
                    Text(
                        "?",
                        style = MaterialTheme.typography.labelSmall.copy(color = secondaryTextColor),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {

                        })
                }

            }
            Box(Modifier.height(paddingLarge))
            Box(Modifier.weight(1f)) {

                ElevatedButton(
                    onClick = {


                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    modifier = Modifier
                        .width(128.dp)
                ) {
                    Text(
                        stringResource(id = R.string.action_next),
                        style = MaterialTheme.typography.labelSmall
                    )


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
//                                profileViewModel.updateCurrentWeight(item)
                            }

                            2 -> {
//                                profileViewModel.updateCurrentHeight(item)
                            }

                        }
//                        profileViewModel.saveProfile()
                        dialogState.value = false

                    },
                    dialogTitle = stringResource(id = inputDialog.value.title),
                    icon = Icons.Default.Info,
                    hint = stringResource(id = inputDialog.value.hint)
                )
            }

        }
    }

}