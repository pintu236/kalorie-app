package com.indieme.kalorie.ui.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.indieme.kalorie.R
import com.indieme.kalorie.data.model.INumberPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberPickerBottomSheet(
    title: String,
    numbersList: List<INumberPicker>,
    onItemSelect: (String) -> Unit,
    onClose: () -> Unit
) {


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
        ) {

            val unitsPickerState = rememberPickerState()

            Text(text = title, modifier = Modifier.padding(top = 16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {

                NumberPickerView(
                    state = unitsPickerState,
                    items = numbersList,
                    visibleItemsCount = 3,
                    modifier = Modifier.weight(0.7f),
                    textModifier = Modifier.padding(8.dp),
                    textStyle = MaterialTheme.typography.labelSmall
                )
            }

            Row() {
                TextButton(
                    onClick = {
                        onClose()
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.action_cancel),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                TextButton(
                    onClick = {
                        onItemSelect(unitsPickerState.selectedItem)
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.action_set),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }


        }
    }
}