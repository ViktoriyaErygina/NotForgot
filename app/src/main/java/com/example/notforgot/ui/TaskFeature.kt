package com.example.notforgot.ui

import android.util.Log
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notforgot.FeatureApi
import com.example.notforgot.ui.card.TaskCardScreen
import com.example.notforgot.ui.list.TaskListStateless

const val TASK_ID = "taskId"

class TaskFeature : FeatureApi {
    // Для списка задач
    private val routePathForTasksScreen = "tasks"
    override fun route() = routePathForTasksScreen

    // Для карточки задачи
    private val routePathForTaskCardScreen = "taskCard"
    private val routeMaskForTaskCardScreen =
        "$routePathForTaskCardScreen/{$TASK_ID}"
    private val routeArgumentsForTaskCardScreen = listOf(
        navArgument(TASK_ID) { type = NavType.IntType }
    )

    private fun routeToTaskCardScreen(taskId: Int? = 0) =
        "$routePathForTaskCardScreen/$taskId"

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        // - Экран списка задач
        navGraphBuilder.composable(
            route = routePathForTasksScreen
        ) {
            TaskListStateless(
                onBackClick = {
                    navController.popBackStack()
                },
                onSelectTaskClick = { id ->
                    navController.navigate(
                        routeToTaskCardScreen(id)
                    )
                },
                onAddTaskClick = {
                    navController.navigate(
                        routeToTaskCardScreen()
                    )
                }
            )
        }
        // - Экран карточки задачи
        navGraphBuilder.composable(
            route = routeMaskForTaskCardScreen,
            arguments = routeArgumentsForTaskCardScreen
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val taskId = arguments.getInt(TASK_ID)
            Log.i("TAG", "registerGraph: $taskId")
            TaskCardScreen(
                taskId = taskId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}