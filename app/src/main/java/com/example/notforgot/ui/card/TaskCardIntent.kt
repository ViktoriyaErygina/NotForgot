package com.example.notforgot.ui.card

import com.example.notforgot.domain.models.Priority

sealed interface TaskCardIntent {
    data class ChangeName(val name: String) : TaskCardIntent
    data class ChangeDescription(val description: String) : TaskCardIntent
    data class ChangeDeadline(val deadline: String) : TaskCardIntent
    data class ChangePriority(val priority: Priority) : TaskCardIntent
    data class ChangeScreenMode(val state: ScreenMode) : TaskCardIntent
    data class InitTask(val id: Int) : TaskCardIntent
    data object SaveTask : TaskCardIntent
}