package com.example.notforgot.domain.repositories

import com.example.notforgot.data.TaskEntity
import com.example.notforgot.domain.models.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun upsertTask(taskDomain: TaskDomain): Int
    fun getAllTasksAsFlow(): Flow<List<TaskDomain>>
    suspend fun getTask(id: Int): TaskDomain
    suspend fun delete(task: TaskDomain)
}