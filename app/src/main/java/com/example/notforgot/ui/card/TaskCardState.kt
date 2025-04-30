package com.example.notforgot.ui.card

import com.example.notforgot.domain.models.Priority

data class TaskCardState(
    val task: TaskState = TaskState(),
    val isTaskNew: Boolean = true,
    val screenState: ScreenMode = ScreenMode.EDIT,
)

data class TaskState(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val priority: Priority? = null,
    val isCompleted: Boolean = false,
    val deadline: String = "",
)

enum class ScreenMode {
    READ, EDIT
}