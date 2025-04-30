package com.example.notforgot.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Query("SELECT * FROM task")
    fun getAllTasksAsFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id=:id")
    suspend fun getTask(id: Int): TaskEntity

    @Delete
    suspend fun delete(task: TaskEntity)
}