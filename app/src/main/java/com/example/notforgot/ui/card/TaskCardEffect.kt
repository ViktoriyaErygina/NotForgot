package com.example.notforgot.ui.card

sealed interface TaskCardEffect {
    data class ShowError(val errorMsg: String) : TaskCardEffect
    data object NavigateBack : TaskCardEffect
}