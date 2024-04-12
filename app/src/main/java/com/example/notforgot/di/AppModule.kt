package com.example.notforgot.di

import androidx.lifecycle.SavedStateHandle
import com.example.notforgot.data.RoomDb
import com.example.notforgot.data.TaskRepositoryImpl
import com.example.notforgot.domain.repositories.TaskRepository
import com.example.notforgot.domain.usecases.DeleteTaskUseCase
import com.example.notforgot.domain.usecases.GetAllTasksAsFlowUseCase
import com.example.notforgot.domain.usecases.GetTaskByIdUseCase
import com.example.notforgot.domain.usecases.UpsertTaskWithValidationUseCase
import com.example.notforgot.ui.TaskFeature
import com.example.notforgot.ui.card.TaskCardViewModel
import com.example.notforgot.ui.list.TasksViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RoomDb.getDatabase(androidContext()) }
    single { get<RoomDb>().taskDao() }
    single { TaskFeature() }
    single { SavedStateHandle() }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    // Room Database
    viewModel { TaskCardViewModel(get(), get(), get()) }
    viewModel { TasksViewModel(get(), get(), get()) }
    factory { UpsertTaskWithValidationUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { GetAllTasksAsFlowUseCase(get()) }
    factory { GetTaskByIdUseCase(get()) }
}