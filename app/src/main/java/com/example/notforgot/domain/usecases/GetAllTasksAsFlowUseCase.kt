package com.example.notforgot.domain.usecases

import com.example.notforgot.domain.repositories.TaskRepository

class GetAllTasksAsFlowUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke() = taskRepository.getAllTasksAsFlow()
}