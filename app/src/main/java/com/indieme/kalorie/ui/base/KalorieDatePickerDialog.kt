package com.indieme.kalorie.ui.base

import android.util.TimeUtils
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.indieme.kalorie.R
import com.indieme.kalorie.utils.TimingUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalorieDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    dateFormat: String = TimingUtils.TimeFormats.DD_MM_YYYY.timeFormat
) {

    val datePickerState = rememberDatePickerState()

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it, dateFormat)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(
                    text = stringResource(id = R.string.action_ok),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(
                    text = stringResource(id = R.string.action_cancel),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun convertMillisToDate(millis: Long, timeFormat: String): String {
    val formatter = SimpleDateFormat(timeFormat, Locale.getDefault())
    return formatter.format(Date(millis))
}