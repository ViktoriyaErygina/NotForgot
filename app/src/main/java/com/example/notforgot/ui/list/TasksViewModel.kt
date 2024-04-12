package com.example.notforgot.ui.list

import android.net.sip.SipSession.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notforgot.data.TaskDao
import com.example.notforgot.data.TaskEntity
import com.example.notforgot.domain.models.TaskDomain
import com.example.notforgot.domain.usecases.DeleteTaskUseCase
import com.example.notforgot.domain.usecases.GetAllTasksAsFlowUseCase
import com.example.notforgot.domain.usecases.UpsertTaskWithValidationUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasksViewModel(
    getAllTasksAsFlowUseCase: GetAllTasksAsFlowUseCase,
    private val upsertTaskWithValidationUseCase: UpsertTaskWithValidationUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    val tasks: StateFlow<List<TaskDomain>> = getAllTasksAsFlowUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    fun onCompletedChange(task: TaskDomain, isCompleted: Boolean) = viewModelScope.launch {
        task.copy(
            isCompleted = isCompleted
        ).let {
            upsertTaskWithValidationUseCase(it)
        }
    }

    fun deleteTask(task: TaskDomain) = viewModelScope.launch {
        deleteTaskUseCase(task)
    }
}

sealed class StateScreen {
    data object Loading : StateScreen()
    data class Success(val tasks: List<TaskDomain>) : StateScreen()
}