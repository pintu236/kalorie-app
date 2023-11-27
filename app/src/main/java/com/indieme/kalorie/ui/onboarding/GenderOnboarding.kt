package com.indieme.kalorie.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.Screen
import com.indieme.kalorie.data.model.UserProfileDTO
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.ui.theme.selectableIconSize

@Composable
fun GenderOnBoardingScreen(parentNavController: NavController) {

    val userProfileDTO = UserProfileDTO(
        "",
        "", null, "", null, null, null, null, "", ""
    )

    var selectableGender by remember {
        mutableStateOf(1)
    }

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
                    stringResource(id = R.string.label_gender),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    stringResource(id = R.string.label_gender_onboarding_info),
                    style = MaterialTheme.typography.labelSmall, color = Color.LightGray,
                )
            }
            Box(Modifier.height(paddingLarge))
            Column(verticalArrangement = Arrangement.Center) {
                Row(Modifier.height(256.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_female),
                        contentDescription = "",
                        modifier = Modifier
                            .size(selectableIconSize)
                            .background(
                                color = if (selectableGender == 1) primaryColor else Color.Transparent,
                                CircleShape
                            )
                            .clickable {
                                selectableGender = 1
                            }
                    )
                    Box(modifier = Modifier.width(paddingLarge))
                    Image(
                        painter = painterResource(id = R.drawable.ic_male),
                        contentDescription = "",
                        modifier = Modifier
                            .size(selectableIconSize)
                            .background(
                                color = if (selectableGender == 2) primaryColor else Color.Transparent,
                                CircleShape
                            )
                            .clickable {
                                selectableGender = 2
                            }
                    )
                }
            }
            Box(Modifier.height(paddingLarge))
            Box(Modifier.weight(1f)) {

                ElevatedButton(
                    onClick = {

                        userProfileDTO.gender = selectableGender
                        parentNavController.navigate(Screen.AgeOnBoarding.route)

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

        }
    }
}