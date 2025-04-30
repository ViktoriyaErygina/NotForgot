package com.example.notforgot.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notforgot.R
import com.example.notforgot.ui.theme.DarkGray
import com.example.notforgot.ui.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    onDeadlineChange: (String) -> Unit,
    closeDialog: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val datePickerWidth = (screenWidthDp * 0.8).dp
    DatePickerDialog(
        modifier = Modifier
            .padding(20.dp)
            .width(datePickerWidth),
        onDismissRequest = closeDialog,
        confirmButton = {
            TextButton(onClick = {
                closeDialog()
                datePickerState.selectedDateMillis?.let { time ->
                    onDeadlineChange(DateUtils.millToStringDateFormat(time))
                }
            }) {
                Text(
                    text = stringResource(id = R.string.ok),
                    color = DarkGray
                )
            }
        },
        dismissButton = {
            TextButton(onClick = closeDialog) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = DarkGray
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color.White
        )
    ) {
        androidx.compose.material3.DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = DarkGray,
                todayDateBorderColor = DarkGray
            )
        )
    }
}