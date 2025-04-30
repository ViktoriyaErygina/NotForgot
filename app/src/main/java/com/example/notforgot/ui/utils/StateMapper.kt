package com.example.notforgot.ui.utils

import com.example.notforgot.domain.models.TaskDomain
import com.example.notforgot.ui.card.TaskState

fun TaskState.toDomain() : TaskDomain {
    val zonedDateTime = DateUtils.stringToZonedDateTime(deadline)
    return TaskDomain(
        id = id,
        name = name,
        description = description,
        priority = priority,
        isCompleted = isCompleted,
        deadline = zonedDateTime
    )
}

fun TaskDomain.toState() : TaskState {
    val deadlineString = DateUtils.zonedDateTimeToString(deadline)
    return TaskState(
        id = id,
        name = name,
        description = description,
        priority = priority,
        isCompleted = isCompleted,
        deadline = deadlineString
    )
}
