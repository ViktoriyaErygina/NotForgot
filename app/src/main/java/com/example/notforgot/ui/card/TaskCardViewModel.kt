package com.example.notforgot.ui.card

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notforgot.data.Priority
import com.example.notforgot.data.TaskDao
import com.example.notforgot.data.TaskEntity
import com.example.notforgot.domain.models.TaskDomain
import com.example.notforgot.domain.usecases.GetTaskByIdUseCase
import com.example.notforgot.domain.usecases.UpsertTaskWithValidationUseCase
import com.example.notforgot.ui.TASK_ID
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val TEMP_ID = 0

class TaskCardViewModel(
    savedStateHandle: SavedStateHandle,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val upsertTaskWithValidationUseCase: UpsertTaskWithValidationUseCase
) : ViewModel() {

    private val taskId = savedStateHandle[TASK_ID] ?: TEMP_ID

    private val _taskState: MutableStateFlow<TaskCardUiState> = MutableStateFlow(TaskCardUiState())
    val taskState: StateFlow<TaskCardUiState> = _taskState

    val errorMsg: MutableStateFlow<String> = MutableStateFlow("")

    fun init(taskId: Int) {
        Log.i(TAG, "taskId: $taskId")
        viewModelScope.launch {
            _taskState.update { prevState ->
                if (taskId > TEMP_ID) {
                    val task = getTaskByIdUseCase(taskId)

                    prevState.copy(
                        task = task,
                        screenState = TaskCardState.READ
                    )
                } else {
                    prevState.copy(
                        screenState = TaskCardState.EDIT
                    )
                }

            }

        }
    }

    fun changeScreenState(screenState: TaskCardState) =
        _taskState.update { prevState ->
            prevState.copy(
                screenState = screenState
            )
    }

    fun onNameChanged(name: String) =
        _taskState.update { prevState ->
            prevState.copy(
                task = prevState.task.copy(
                    name = name
                )
            )
        }

    fun onDescriptionChanged(description: String) =
        _taskState.update { prevState ->
            prevState.copy(
                task = prevState.task.copy(
                    description = description
                )
            )
        }

    fun onPriorityChanged(priority: Priority) =
        _taskState.update { prevState ->
            prevState.copy(
                task = prevState.task.copy(
                    priority = priority
                )
            )
        }

    fun saveTask(): Deferred<Result<Int>> = viewModelScope.async {
        val task = taskState.value.task
        upsertTaskWithValidationUseCase(task).onFailure { error ->
            errorMsg.update { error.message ?: "" }
        }
    }

    fun onDeadlineChange(deadline: String) = _taskState.update { prevState ->
        prevState.copy(
            task = prevState.task.copy(
                deadline = deadline
            )
        )
    }

    fun clearErrorMsg() = errorMsg.update { "" }

    companion object {
        private val TAG = TaskCardViewModel::class.java.simpleName
    }
}

data class TaskCardUiState(
    val task: TaskDomain = TaskDomain(),
    val screenState: TaskCardState = TaskCardState.EDIT
)

enum class TaskCardState {
    READ, EDIT
}