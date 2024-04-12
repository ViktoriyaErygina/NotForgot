package com.example.notforgot.data

import com.example.notforgot.domain.models.TaskDomain

fun TaskDomain.toEntity(): TaskEntity =
    TaskEntity(
        id = id,
        name = name,
        description = description,
        priority = priority ?: Priority.URGENTLY,
        isCompleted = isCompleted,
        deadline = deadline,
    )

fun TaskEntity.toDomain(): TaskDomain =
    TaskDomain(
        id = id,
        name = name,
        description = description,
        priority = priority,
        isCompleted = isCompleted,
        deadline = deadline,
    )