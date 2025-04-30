package com.example.notforgot.domain.usecases

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.notforgot.domain.models.TaskDomain
import com.example.notforgot.domain.repositories.TaskRepository

class UpsertTaskWithValidationUseCase(
    private val taskRepository: TaskRepository,
    private val setAlarmForNotification: SetAlarmForNotification
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(taskDomain: TaskDomain): Result<Int> {
        if (taskDomain.name.isEmpty()) {
            return Result.failure(Exception("Введите название задачи"))
        }
        if (taskDomain.description.isEmpty()) {
            return Result.failure(Exception("Введите описание задачи"))
        }
        if (taskDomain.priority == null) {
            return Result.failure(Exception("Выберете приоритет задачи"))
        }
        taskRepository.upsertTask(taskDomain)
        setAlarmForNotification(, taskDomain.deadline)
        return Result.success(0)
    }
}