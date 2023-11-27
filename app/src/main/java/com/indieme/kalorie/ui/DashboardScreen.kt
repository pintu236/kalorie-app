package com.indieme.kalorie.ui

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.indieme.kalorie.R
//import com.indieme.kalorie.R
import com.indieme.kalorie.Screen
import com.indieme.kalorie.ui.favorite.FavoriteScreen
import com.indieme.kalorie.ui.home.HomeScreen
import com.indieme.kalorie.ui.profile.ProfileScreen
import com.indieme.kalorie.ui.scan.ScanScreen
import com.indieme.kalorie.ui.search.SearchScreen
import com.indieme.kalorie.ui.theme.iconColor
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.ui.trackfood.TrackFoodCalorieScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(parentNavController: NavController) {

    val navController = rememberNavController()
    val openBottomSheet = rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded = remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded.value
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(Screen.Home, Screen.Search, Screen.Scan, Screen.Favorite, Screen.Profile)


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    getTitleFromRoute(currentDestination?.route),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            })
        }, bottomBar = {
            Box {

                NavigationBar(containerColor = Color.Transparent) {


                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = iconColor,
                                indicatorColor = primaryColor
                            ),
                            icon = {
                                if (index != 2) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = item.defaultResId!!),
                                        contentDescription = item.route,
                                    )
                                }
                            },

                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                if (index == 2) {
                                    openBottomSheet.value = true
                                } else {
                                    navController.navigate(item.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }

                            }
                        )
                    }
                }
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(primaryColor)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.round_add_24),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center),
                        tint = Color.White
                    )
                }
            }
        }) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(route = Screen.Search.route) {
                SearchScreen(navController = navController)
            }
            composable(route = Screen.Scan.route) {
                ScanScreen(navController = navController)
            }
            composable(route = Screen.Favorite.route) {
                FavoriteScreen(navController = navController)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(navController = navController, parentNavController)
            }
            composable(route = Screen.TrackFoodCalorie.route) {
                TrackFoodCalorieScreen(parentNavController = parentNavController)
            }

        }
    }

    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            Row(modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .fillMaxWidth()
                .clickable {

                }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.fi_camera),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan Or Capture")
            }

            Row(modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .fillMaxWidth()
                .clickable {

                }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.u_mouse_alt),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Manual")
            }
        }
    }


}

fun getTitleFromRoute(route: String?): String {
    return when (route) {
        Screen.Home.route -> {
            "Home"
        }

        Screen.Search.route -> {
            "Search"
        }

        Screen.Favorite.route -> {
            "Favorites"
        }

        Screen.Profile.route -> {
            "Profile"
        }

        else -> {
            "Home"
        }
    }
}

