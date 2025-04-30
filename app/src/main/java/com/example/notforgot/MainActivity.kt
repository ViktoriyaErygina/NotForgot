package com.example.notforgot

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.notforgot.ui.TaskFeature
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { paddingValues ->
                paddingValues
                AppNavGraph(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    val taskFeature: TaskFeature = koinInject()

    NavHost(
        navController = navController,
        startDestination = taskFeature.route()
    ) {
        register(
            taskFeature,
            navController
        )
    }
}

fun NavGraphBuilder.register(
    featureApi: FeatureApi,
    navController: NavHostController
) {
    featureApi.registerGraph(
        navGraphBuilder = this,
        navController = navController
    )
}

interface FeatureApi {
    // Название маршрута
    fun route(): String

    // Регистрация графа
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    )
}


