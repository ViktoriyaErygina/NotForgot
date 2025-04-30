package com.example.notforgot.domain.usecases

import com.example.notforgot.domain.models.TaskDomain
import com.example.notforgot.domain.repositories.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskDomain: TaskDomain) = taskRepository.delete(taskDomain)
}