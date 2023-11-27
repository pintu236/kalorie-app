package com.indieme.kalorie.ui.register


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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.indieme.kalorie.Screen
import com.indieme.kalorie.data.manager.LocalPreferenceManager
import com.indieme.kalorie.ui.theme.primaryColor
import com.indieme.kalorie.utils.AppUtil
import com.indieme.kalorie.utils.K_EMAIL
import com.indieme.kalorie.utils.K_PASSWORD
import com.indieme.kalorie.utils.K_USERNAME
import java.util.regex.Matcher
import java.util.regex.Pattern


@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = viewModel()) {
    var loadingState = remember {
        mutableStateOf(false)
    };
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.userState.collect() { uiState ->
            when (uiState) {
                RegisterUiState.Empty -> Unit
                RegisterUiState.Loading -> {
                    loadingState.value = true;
                }

                is RegisterUiState.Error -> {
                    loadingState.value = false;
                    AppUtil.showMessage(context, uiState.exception)
                }

                is RegisterUiState.Success -> {
                    loadingState.value = false;
                    LocalPreferenceManager.saveUserData(context, uiState.userDTO)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(0)
                    }
                }
            }

        }
    }
    val usernameValue = remember { mutableStateOf("") }
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {

            Text(text = "Get Started", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "by creating free account",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Gray
            )

            Box(Modifier.padding(24.dp))
            OutlinedTextField(usernameValue.value, {
                usernameValue.value = it
            }, label = {
                Text("Username")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ), modifier = Modifier.fillMaxWidth(), trailingIcon = {
                Icon(Icons.Outlined.Person, "person")
            })
            Box(Modifier.padding(8.dp))

            OutlinedTextField(emailValue.value, {
                emailValue.value = it
            }, label = {
                Text("Email")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ), modifier = Modifier.fillMaxWidth(), trailingIcon = {
                Icon(Icons.Outlined.Email, "email")
            })
            Box(Modifier.padding(8.dp))
            OutlinedTextField(passwordValue.value,
                {
                    passwordValue.value = it
                },
                label = {
                    Text("Password")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(Icons.Outlined.Lock, "lock")
                })
            Box(Modifier.padding(8.dp))

            OutlinedTextField(confirmPasswordValue.value,
                {
                    confirmPasswordValue.value = it
                },
                label = {
                    Text("Confirm Password")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(Icons.Outlined.Lock, "lock")
                })
            Box(Modifier.padding(24.dp))
            ElevatedButton(
                onClick = {
//                    if (usernameValue.value.trim().isEmpty()) {
//                        AppUtil.showMessage(context, "Username field is required")
//                        return@ElevatedButton
//                    } else if (emailValue.value.trim().isEmpty()) {
//                        AppUtil.showMessage(context, "Email field is required")
//                        return@ElevatedButton
//                    } else if (passwordValue.value.trim().isEmpty()) {
//                        AppUtil.showMessage(context, "Password field is required")
//                        return@ElevatedButton
//                    } else if (confirmPasswordValue.value.trim() != passwordValue.value.trim()) {
//                        AppUtil.showMessage(context, "Password and Confirm Password mismatch")
//                        return@ElevatedButton
//                    } else if (!isEmailValid(emailValue.value.trim())) {
//                        AppUtil.showMessage(context, "Email address is invalid")
//                        return@ElevatedButton
//                    }
                    val hashMap = HashMap<String, Any>()

                    hashMap[K_USERNAME] = usernameValue.value.trim()
                    hashMap[K_EMAIL] = emailValue.value.trim()
                    hashMap[K_PASSWORD] = passwordValue.value.trim()

//                    viewModel.registerUser(hashMap)
                    navController.navigate(Screen.GenderOnBoarding.route)

                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (loadingState.value) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Register", style = MaterialTheme.typography.labelLarge)
                }


            }
            Box(Modifier.padding(8.dp))
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Text(text = "Log In",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                    color = primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Login.route)
                    })
            }

        }
    }

}

/**
 * validate your email address format. Ex-akhi@mani.com
 */
fun isEmailValid(email: String?): Boolean {
    val pattern: Pattern
    val EMAIL_PATTERN =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    pattern = Pattern.compile(EMAIL_PATTERN)
    val matcher: Matcher = pattern.matcher(email.toString())
    return matcher.matches()
}


