package com.example.notforgot.ui.card

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.notforgot.R
import com.example.notforgot.data.Priority
import com.example.notforgot.domain.models.TaskDomain
import com.example.notforgot.ui.theme.Blue
import com.example.notforgot.ui.theme.CompletedColor
import com.example.notforgot.ui.theme.DarkGray
import com.example.notforgot.ui.theme.DropDownColor
import com.example.notforgot.ui.theme.EditColor
import com.example.notforgot.ui.theme.Gray
import com.example.notforgot.ui.theme.NotCompleted
import com.example.notforgot.ui.theme.NotForgotTheme
import com.example.notforgot.ui.theme.TextColorGray
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCardScreen(
    taskId: Int,
    onBackClick: () -> Unit,
) {
    val viewModel: TaskCardViewModel = koinInject()
    LaunchedEffect(taskId) {
        viewModel.init(taskId)
    }
    val uiState by viewModel.taskState.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()
    val task = remember(uiState.task) { uiState.task }
    val screenState = remember(uiState.screenState) { uiState.screenState }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(errorMsg) {
        if (errorMsg.isNotEmpty()) {
            snackBarHostState.showSnackbar(errorMsg)
        }
        viewModel.clearErrorMsg()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val topBarText = when (screenState) {
                        TaskCardState.READ -> stringResource(id = R.string.note)
                        TaskCardState.EDIT -> {
                            if (taskId != 0) stringResource(id = R.string.note)
                            else stringResource(id = R.string.add_note)
                        }
                    }
                    Text(text = topBarText)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = stringResource(id = R.string.back),
                            tint = Gray
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                when (screenState) {
                    TaskCardState.READ -> {
                        ReadCard(
                            task = task,
                            onEditTaskClick = { viewModel.changeScreenState(TaskCardState.EDIT) }
                        )
                    }

                    TaskCardState.EDIT -> {
                        EditCard(
                            name = remember(task.name) { task.name },
                            description = remember(task.description) { task.description },
                            deadline = remember(task.deadline) { task.deadline },
                            priority = remember(task.priority) { task.priority },
                            onTaskNameChange = remember {
                                {
                                    viewModel.onNameChanged(it)
                                }
                            },
                            onTaskDescriptionChange = remember {
                                {
                                    viewModel.onDescriptionChanged(it)
                                }
                            },
                            onDeadlineChange = remember {
                                {
                                    viewModel.onDeadlineChange(it)
                                }
                            },
                            onPriorityChange = remember {
                                {
                                    viewModel.onPriorityChanged(it)
                                }
                            },
                            onSaveClick = {
                                scope.launch {
                                    val result = viewModel.saveTask().await()
                                    if (result.isSuccess) onBackClick()
                                }

                            }
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                SnackbarError(it)
            }
        }
    )
}

@Composable
fun ReadCard(
    task: TaskDomain,
    onEditTaskClick: (id: Int) -> Unit,
) {
    val scrollState = rememberScrollState()
    Log.i("TAG", "ReadCard: ${task.deadline}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = task.name,
                fontSize = 24.sp,
                fontWeight = FontWeight(700)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onEditTaskClick(task.id)
                }
            ) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = stringResource(id = R.string.edit),
                    tint = EditColor
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 16.dp),
            text = if (task.isCompleted) stringResource(id = R.string.completed)
            else stringResource(id = R.string.not_completed),
            color = if (task.isCompleted) CompletedColor
            else NotCompleted,
            textAlign = TextAlign.End
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = task.description,
            fontSize = 16.sp
        )
        Row {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                painter = painterResource(id = R.drawable.watch_24),
                contentDescription = stringResource(id = R.string.deadline),
                tint = DarkGray
            )
            Text(
                text = if(!task.deadline.isNullOrEmpty())  "До ${task.deadline}"
                else stringResource(id = R.string.deadline_has_not_been_selected),
                color = TextColorGray,
                fontSize = 14.sp,
                fontWeight = FontWeight(600)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.flag),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(id = task.priority?.colorId ?: Priority.URGENTLY.colorId))
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = task.priority?.description ?: Priority.URGENTLY.description,
                    color = Color.White,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCard(
    name: String,
    description: String,
    deadline: String?,
    priority: Priority?,
    onTaskNameChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit,
    onDeadlineChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onSaveClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var openDialog by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(deadline ?: "") }
    LaunchedEffect(date) {
        onDeadlineChange(date)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = stringResource(id = R.string.heading),
            fontSize = 18.sp,
            fontWeight = FontWeight(600)
        )
        // - Название задачи
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = name,
            onValueChange = onTaskNameChange,
            label = {
                Text(text = stringResource(id = R.string.name))
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )
        // - Описание
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = onTaskDescriptionChange,
            label = {
                Text(text = stringResource(id = R.string.description))
            },
            minLines = 5
        )
        // - Количество символов
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${description.length}",
            fontSize = 12.sp,
            textAlign = TextAlign.End
        )
        // - Срок выполнения задачи
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        ) {
            TextField(
                value = deadline ?: date,
                onValueChange = { date = it },
                label = { Text(stringResource(R.string.deadline)) },
                readOnly = true,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            IconButton(
                onClick = {
                    openDialog = true
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.date_range_24px),
                    contentDescription = stringResource(id = R.string.select_date),
                    tint = Blue
                )
            }
        }
        // - Приоритет задачи
        DropDownMenu(
            taskPriority = priority,
            onItemClick = onPriorityChange
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray
            ),
            onClick = onSaveClick
        ) {
            Text(
                text = stringResource(id = R.string.save),
                color = Color.White
            )
        }

        if (openDialog) {
            val datePickerState = rememberDatePickerState()

            val screenWidthDp = LocalConfiguration.current.screenWidthDp
            val datePickerWidth = (screenWidthDp * 0.8).dp
            DatePickerDialog(
                modifier = Modifier
                    .padding(20.dp)
                    .width(datePickerWidth),
                onDismissRequest = { openDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        openDialog = false
                        datePickerState.selectedDateMillis?.let { time ->
                            val selectedDate = Date(time)
                            val formatter = SimpleDateFormat("dd.MM.yyyy")
                            date = formatter.format(selectedDate)
                        }
                    }) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            color = DarkGray
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        openDialog = false
                    }) {
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
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = DarkGray,
                        todayDateBorderColor = DarkGray
                    )
                )
            }
        }

    }
}

@Composable
fun DropDownMenu(
    taskPriority: Priority?,
    onItemClick: (Priority) -> Unit,
) {
    var dropDownExpanded by remember {
        mutableStateOf(false)
    }
    val icon = if (dropDownExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column(
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .clickable {
                    dropDownExpanded = !dropDownExpanded
                }
                .clip(RoundedCornerShape(8.dp))
                .padding(vertical = 20.dp),
            value = taskPriority?.description ?: "",
            onValueChange = {},
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            label = {
                Text(text = stringResource(id = R.string.priority))
            },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(
                        R.string.arrow_icon_in_drop_down_list_description
                    )
                )
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = DropDownColor,
                disabledIndicatorColor = Color.Transparent
            ),
            enabled = false,
            singleLine = true,
            maxLines = 1
        )
        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = { dropDownExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            Priority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = {
                        Text(text = priority.description)
                    },
                    onClick = {
                        dropDownExpanded = false
                        onItemClick(priority)
                    }
                )
            }
        }
    }
}

@Composable
fun SnackbarError(
    data: SnackbarData,
    onClick: () -> Unit = { data.dismiss() }
) {
    Snackbar(
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cancel_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = data.visuals.message,
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Preview
@Composable
fun EditCardPreview() {
    NotForgotTheme {
        EditCard(
            name = "",
            description = "",
            priority = Priority.URGENTLY,
            deadline = "",
            onTaskNameChange = {},
            onTaskDescriptionChange = {},
            onDeadlineChange = {},
            onPriorityChange = {},
            onSaveClick = {}
        )
    }
}
