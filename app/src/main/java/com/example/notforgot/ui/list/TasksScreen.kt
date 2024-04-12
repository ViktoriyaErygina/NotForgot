package com.example.notforgot.ui.list

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notforgot.R
import com.example.notforgot.data.Priority
import com.example.notforgot.domain.models.TaskDomain
import com.example.notforgot.ui.theme.DarkGray
import com.example.notforgot.ui.theme.Gray
import com.example.notforgot.ui.theme.NotForgotTheme
import com.example.notforgot.ui.theme.SwipeToEndColor
import com.example.notforgot.ui.theme.SwipeToStartColor
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListStateless(
    onBackClick: () -> Unit,
    onAddTaskClick: () -> Unit,
    onSelectTaskClick: (id: Int) -> Unit
) {
    val viewModel: TasksViewModel = koinInject()
    val tasks by viewModel.tasks.collectAsState()
    val activity = (LocalContext.current as Activity)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.not_forgot),
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            activity.finish()
                        }
                    ) {
                        Icon(
                            // painterResource(id = R.drawable.back_icon)
                            imageVector = Icons.Outlined.ExitToApp,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Gray
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (tasks.isEmpty()) {
                    EmptyTaskList()
                } else {
                    TasksListContent(
                        tasks = tasks,
                        onSelectTaskClick = onSelectTaskClick,
                        onCompletedChanged = { task, isCompleted ->
                            viewModel.onCompletedChange(task, isCompleted)
                        },
                        onDeleteTask = {
                            viewModel.deleteTask(it)
                        }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = DarkGray,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = stringResource(id = R.string.add_task)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksListContent(
    tasks: List<TaskDomain>,
    onSelectTaskClick: (id: Int) -> Unit,
    onCompletedChanged: (task: TaskDomain, isCompleted: Boolean) -> Unit,
    onDeleteTask: (task: TaskDomain) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = stringResource(id = R.string.tasks),
            fontSize = 24.sp,
            fontWeight = FontWeight(700)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                items = tasks,
                key = {index, item ->
                    item.hashCode()
                }
            ) { index, task ->
                val state = rememberDismissState(
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            onDeleteTask(task)
                        }
//                        when (it) {
//                            DismissValue.DismissedToEnd -> onSelectTaskClick(task.id)
//                            DismissValue.DismissedToStart -> onDeleteTask(task)
//                            DismissValue.Default -> {}
//                        }
                        false
                    }
                )
                SwipeToDismiss(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    state = state,
                    background = {
                        val color = when (state.dismissDirection) {
                            DismissDirection.StartToEnd -> SwipeToEndColor
                            DismissDirection.EndToStart -> SwipeToStartColor
                            null -> Color.Transparent
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SwipeToStartColor)
                                .padding(12.dp, 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }
                    },
                    directions = setOf(DismissDirection.EndToStart),
                    dismissContent = {
                        ListItem(task, onSelectTaskClick, onCompletedChanged)
                    }
                )
            }
        }
    }

}

@Composable
fun ListItem(
    task: TaskDomain,
    onSelectTaskClick: (id: Int) -> Unit,
    onCompletedChanged: (task: TaskDomain, isCompleted: Boolean) -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxSize()
            /*.padding(bottom = 8.dp)*/,
        onClick = {
            onSelectTaskClick(task.id)
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = task.priority?.colorId ?: Priority.URGENTLY.colorId),
            contentColor = Color.White
        )
    ) {

        Row {
            Column {
                Text(
                    text = task.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(700),
                    color = Color.White
                )
                Text(
                    text = task.description,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isCompleted ->
                    onCompletedChanged(task, isCompleted)
                },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = colorResource(id = task.priority?.colorId ?: Priority.URGENTLY.colorId),
                    checkedColor = Color.White,
                    uncheckedColor = Color.White
                )
            )
        }
    }
}

@Composable
fun EmptyTaskList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.relax_image),
            contentDescription = stringResource(id = R.string.relax_image)
        )
        Text(
            modifier = Modifier.padding(horizontal = 70.dp),
            text = stringResource(id = R.string.no_tasks),
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun TaskListPreview() {
    NotForgotTheme {
        TaskListStateless(
            onBackClick = {},
            onAddTaskClick = {},
            onSelectTaskClick = {}
        )
    }
}