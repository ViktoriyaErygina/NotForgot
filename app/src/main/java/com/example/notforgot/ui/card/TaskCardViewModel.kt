package com.example.notforgot.ui.card

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notforgot.domain.models.Priority
import com.example.notforgot.domain.usecases.GetTaskByIdUseCase
import com.example.notforgot.domain.usecases.SetAlarmForNotification
import com.example.notforgot.domain.usecases.UpsertTaskWithValidationUseCase
import com.example.notforgot.ui.TASK_ID
import com.example.notforgot.ui.utils.toDomain
import com.example.notforgot.ui.utils.toState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val NEW_TASK_ID = 0

class TaskCardViewModel(
    savedStateHandle: SavedStateHandle,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val upsertTaskWithValidationUseCase: UpsertTaskWithValidationUseCase,
    private val setAlarmForNotification: SetAlarmForNotification
) : ViewModel() {

    private val dbState = MutableStateFlow(TaskState())
    private val _uiState = MutableStateFlow(TaskCardState())
    val uiState: StateFlow<TaskCardState> = _uiState

    private val _effect = MutableSharedFlow<TaskCardEffect>()
    val effect: SharedFlow<TaskCardEffect>
        get() = _effect.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000L))

    fun reduce(intent: TaskCardIntent) = viewModelScope.launch {
        when (intent) {
            is TaskCardIntent.ChangeName -> changeName(intent.name)
            is TaskCardIntent.ChangeDescription -> changeDescription(intent.description)
            is TaskCardIntent.ChangeDeadline -> changeDeadline(intent.deadline)
            is TaskCardIntent.ChangePriority -> changePriority(intent.priority)
            is TaskCardIntent.ChangeScreenMode -> changeScreenMode(intent.state)
            is TaskCardIntent.InitTask -> initTask(intent.id)
            TaskCardIntent.SaveTask -> saveTask()
        }
    }

    private fun initTask(id: Int) = viewModelScope.launch {
        _uiState.update { prevState ->
            if (id > NEW_TASK_ID) {
                val task = getTaskByIdUseCase(id)

                prevState.copy(
                    task = task.toState(),
                    isTaskNew = false,
                    screenState = ScreenMode.READ
                )
            } else {
                prevState.copy(
                    isTaskNew = true,
                    screenState = ScreenMode.EDIT
                )
            }

        }

    }

    private fun changeScreenMode(screenState: ScreenMode) =
        _uiState.update { prevState ->
            prevState.copy(
                screenState = screenState
            )
    }

    private fun changeName(name: String) =
        _uiState.update { prevState ->
            prevState.copy(
                task = prevState.task.copy(
                    name = name
                )
            )
        }

    private fun changeDescription(description: String) =
        _uiState.update { prevState ->
            prevState.copy(
                task = prevState.task.copy(
                    description = description
                )
            )
        }

    private fun changePriority(priority: Priority) =
        _uiState.update { prevState ->
            prevState.copy(
                task = prevState.task.copy(
                    priority = priority
                )
            )
        }

    private fun saveTask() = viewModelScope.launch {
        Log.i(TAG, "saveTask: ${Thread.currentThread().name}")
        val task = uiState.value.task
        upsertTaskWithValidationUseCase(task.toDomain())
            .onSuccess { newId ->
                dbState.update { _uiState.value.task }
                if (_uiState.value.isTaskNew) {
                    _effect.emit(TaskCardEffect.NavigateBack)
                } else {
                    changeScreenMode(ScreenMode.READ)
                }
            }
            .onFailure { error ->
                _effect.emit(
                    TaskCardEffect.ShowError(error.message ?: "Неожиданная ошибка")
                )
        }
    }

    private fun changeDeadline(deadline: String) = _uiState.update { prevState ->
        prevState.copy(
            task = prevState.task.copy(
                deadline = deadline
            )
        )
    }

    companion object {
        private val TAG = TaskCardViewModel::class.java.simpleName
    }
}