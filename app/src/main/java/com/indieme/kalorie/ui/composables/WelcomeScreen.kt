package com.indieme.kalorie.ui.composables


import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.Screen
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.ui.theme.secondaryColor
import com.indieme.kalorie.ui.theme.secondaryColorVariant


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(navController: NavController) {
    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Kalorie")
        Box(Modifier.padding(24.dp))
        HorizontalPager(pageCount = 3, state = pagerState) { page ->
            PagerItem(page = page)
        }
        Box(Modifier.padding(8.dp))
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) secondaryColor else secondaryColorVariant
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color)
                        .width(30.dp)
                        .height(15.dp)

                )
            }
        }
        Box(Modifier.padding(24.dp))
        ElevatedButton(
            onClick = {
                navController.navigate(Screen.Register.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Get Started", style = MaterialTheme.typography.labelLarge)
        }
        Box(Modifier.padding(8.dp))
        Row() {
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Log In",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                color = primaryColor,
                textAlign = TextAlign.Center, modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

    }

}


@Composable
fun PagerItem(page: Int) {
    when (page) {
        0 -> {
            WelcomePagerItem(
                R.drawable.eating_healthy_food_logo,
                "Eat Healthy",
                "Maintain good health should be the primary focus of everyone."
            )
        }

        1 -> {

            WelcomePagerItem(
                R.drawable.cooking_cuate,
                "Healthy Recipes",
                "Browse thousands of healthy recipes from all over the world."
            )
        }

        else -> {
            WelcomePagerItem(
                R.drawable.mobile_cuate_logo, "Track Your Health",
                "With amazing inbuilt tools you can track your progress"
            )
        }
    }


}

@Composable
private fun WelcomePagerItem(drawableResId: Int, title: String, message: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = drawableResId), "", Modifier.size(282.dp))
        Box(Modifier.padding(24.dp))
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Box(Modifier.padding(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

