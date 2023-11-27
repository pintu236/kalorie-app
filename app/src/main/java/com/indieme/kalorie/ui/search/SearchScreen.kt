package com.indieme.kalorie.ui.search

import android.graphics.Color.GREEN
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.indieme.kalorie.R
import com.indieme.kalorie.ui.theme.defaultCornerSize
import com.indieme.kalorie.ui.theme.fieldBackGroundColor
import com.indieme.kalorie.ui.theme.paddingLarge
import com.indieme.kalorie.ui.theme.primaryColor

@Composable
fun SearchScreen(navController: NavController) {
    val searchValue = remember { mutableStateOf("") }

    Column(Modifier.padding(paddingLarge)) {
        OutlinedTextField(
            searchValue.value,
            {
                searchValue.value = it
            },
            placeholder = { Text(text = stringResource(id = R.string.hint_search_food)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(defaultCornerSize),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = fieldBackGroundColor,
                focusedContainerColor = fieldBackGroundColor,
                focusedIndicatorColor = fieldBackGroundColor,
                unfocusedIndicatorColor = fieldBackGroundColor
            ),
            leadingIcon = {
                Icon(Icons.Outlined.Search, "Search")
            },
        )
    }
}