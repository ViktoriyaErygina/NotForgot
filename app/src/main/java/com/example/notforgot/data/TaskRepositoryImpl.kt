package com.example.notforgot.data

import com.example.notforgot.domain.models.TaskDomain
import com.example.notforgot.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao
): TaskRepository {
    override suspend fun upsertTask(taskDomain: TaskDomain): Int =
        if (taskDomain.id == 0) {
            taskDao.insert(taskDomain.toEntity()).toInt()
        } else {
            taskDao.update(taskDomain.toEntity())
            taskDomain.id
        }


    override fun getAllTasksAsFlow(): Flow<List<TaskDomain>> = taskDao.getAllTasksAsFlow().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun getTask(id: Int): TaskDomain = taskDao.getTask(id).toDomain()

    override suspend fun delete(task: TaskDomain) = taskDao.delete(task.toEntity())
}