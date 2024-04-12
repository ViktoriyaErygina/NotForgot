package com.example.notforgot.data

import androidx.annotation.ColorRes
import com.example.notforgot.R

enum class Priority(
    val id: Int,
    val description: String,
    @ColorRes val colorId: Int
) {
    URGENTLY(
        id = 0,
        description ="Срочно",
        colorId = R.color.urgently
    ),
    IMPORTANT(
        id = 1,
        description = "Важно",
        colorId = R.color.important
    ),
    URGENT_AND_UNIMPORTANT(
        id = 2,
        description = "Неважно",
        colorId = R.color.urgent_and_unimportant
    ),
    NOT_URGENT(
        id = 3,
        description = "Несрочно",
        colorId = R.color.not_urgent
    )
}