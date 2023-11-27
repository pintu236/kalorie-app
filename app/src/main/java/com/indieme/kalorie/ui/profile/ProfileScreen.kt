package com.indieme.kalorie.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.Screen
import com.indieme.kalorie.data.manager.LocalPreferenceManager
import com.indieme.kalorie.data.model.MenuItemModel
import com.indieme.kalorie.ui.theme.fontSizeLarge
import com.indieme.kalorie.ui.theme.iconCornerSize
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.paddingMedium
import com.indieme.kalorie.ui.theme.secondaryColor

val menuItems = mutableListOf(
    MenuItemModel(1, R.string.label_edit_profile, Icons.Default.Person),
    MenuItemModel(2, R.string.label_renew_plans, Icons.Default.Star),
    MenuItemModel(3, R.string.label_settings, Icons.Default.Settings),
    MenuItemModel(4, R.string.label_terms_and_privacy, Icons.Default.AccountBox),
    MenuItemModel(5, R.string.label_log_out, Icons.Default.Build),
)

@Composable
fun ProfileScreen(navController: NavController, parentNavController: NavController) {

    val context = LocalContext.current;
    var logOutState by remember { mutableStateOf(0) }

    LaunchedEffect(logOutState) {

        if (logOutState == 1) {
            //Remove User
            LocalPreferenceManager.removeUserData(context)
            // Call the suspend function here
            parentNavController.navigate(Screen.Splash.route) {
                popUpTo(0)
            }
        }

    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_demo_profile),
            contentDescription = "",
            modifier = Modifier
                .width(128.dp)
                .height(128.dp)
        )
        Box(Modifier.height(paddingLarge))
        Text("Test User", style = MaterialTheme.typography.labelLarge)
        menuItems.forEach {
            Box(Modifier.height(paddingLarge))
            MenuItem(it) { id ->
                when (id) {
                    1 -> parentNavController.navigate(Screen.EditProfile.route)
                    5 -> {
                        logOutState = 1
                    }
                }
            }
        }
    }

}

@Composable
fun MenuItem(menuItemModel: MenuItemModel, onItemSelect: (id: Int) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable {
                onItemSelect(menuItemModel.id)
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            menuItemModel.icon, "", Modifier
                .background(secondaryColor.copy(alpha = 0.75f), RoundedCornerShape(iconCornerSize))
                .padding(paddingMedium)
        )
        Box(modifier = Modifier.width(paddingLarge))
        Text(
            stringResource(id = menuItemModel.title),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = fontSizeLarge),
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Default.KeyboardArrowRight, "", tint = secondaryColor)
    }

}