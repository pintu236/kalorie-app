package com.indieme.kalorie.ui.login


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.indieme.kalorie.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.indieme.kalorie.data.manager.LocalPreferenceManager
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.K_EMAIL
import com.indieme.kalorie.utils.K_PASSWORD
import com.indieme.kalorie.utils.K_USERNAME


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val loadingState = remember {
        mutableStateOf(false)
    };
    val context = LocalContext.current
    val usernameValue = remember { mutableStateOf("") }
    var emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.userState.collect() { uiState ->
            when (uiState) {
                LoginUiState.Empty -> Unit
                is LoginUiState.Error -> {
                    loadingState.value = false;
                    AppUtil.showMessage(context, uiState.exception)
                }

                LoginUiState.Loading -> {
                    loadingState.value = true;
                }

                is LoginUiState.Success -> {
                    loadingState.value = false;
                    LocalPreferenceManager.saveUserData(context, uiState.userDTO)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(0)
                    }

                }
            }

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {

        Text(text = "Welcome Back!!!", style = MaterialTheme.typography.headlineLarge)
        Text(
            text = "Login", style = MaterialTheme.typography.headlineSmall, color = Color.Gray
        )

        Box(Modifier.padding(24.dp))
        OutlinedTextField(usernameValue.value, {
            usernameValue.value = it
        }, label = {
            Text("Username/Email")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
        ), modifier = Modifier.fillMaxWidth(), trailingIcon = {
            Icon(Icons.Outlined.Person, "person")
        })
        Box(Modifier.padding(8.dp))
        OutlinedTextField(passwordValue.value,
            {
                passwordValue.value = it
            },
            label = {
                Text("Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Outlined.Lock, "lock")
            })
        Box(Modifier.padding(24.dp))
        ElevatedButton(
            onClick = {

                val hashMap = HashMap<String, Any>()

                hashMap[K_EMAIL] = usernameValue.value.trim()
                hashMap[K_PASSWORD] = passwordValue.value.trim()

                viewModel.loginUser(hashMap)

            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (loadingState.value) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Login", style = MaterialTheme.typography.labelLarge)
            }

        }
        Box(Modifier.padding(8.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Text(text = "Register",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                color = primaryColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Register.route)
                })
        }

    }

}


