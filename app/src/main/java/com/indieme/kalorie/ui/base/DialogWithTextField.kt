package com.indieme.kalorie.ui.base

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.indieme.kalorie.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogWithTextField(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    dialogTitle: String,
    hint: String,
    icon: ImageVector,
) {
    var text by remember { mutableStateOf("") }


    AlertDialog(icon = {
        Icon(icon, contentDescription = "Example Icon")
    }, title = {
        Text(text = dialogTitle)
    }, text = {
        TextField(value = text, placeholder = {
            Text(hint)
        }, onValueChange = {
            text = it
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ), modifier = Modifier.fillMaxWidth()
        )
    }, onDismissRequest = {

    }, confirmButton = {
        TextButton(onClick = {
            if (text.isEmpty()) {
                onDismissRequest()
            } else {
                onConfirmation(text)
            }

        }) {
            Text(
                stringResource(id = R.string.action_confirm),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            Text(
                stringResource(id = R.string.action_cancel),
                style = MaterialTheme.typography.labelSmall
            )
        }
    })
}