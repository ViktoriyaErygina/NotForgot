package com.example.notforgot.domain.models

import java.time.ZonedDateTime

data class TaskDomain (
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val priority: Priority? = null,
    val isCompleted: Boolean = false,
    val deadline: ZonedDateTime? = null
)