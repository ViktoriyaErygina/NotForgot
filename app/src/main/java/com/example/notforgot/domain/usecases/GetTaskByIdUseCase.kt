package com.example.notforgot.domain.usecases

import com.example.notforgot.domain.repositories.TaskRepository

class GetTaskByIdUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int) = taskRepository.getTask(id)
}