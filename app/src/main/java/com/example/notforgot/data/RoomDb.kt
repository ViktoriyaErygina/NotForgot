package com.example.notforgot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        TaskEntity::class
    ],
    version = 1
)
@TypeConverters(DbConverters::class)
abstract class RoomDb : RoomDatabase(){
    abstract fun taskDao() : TaskDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDb? = null

        fun getDatabase(context: Context): RoomDb {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, RoomDb::class.java, "contact_database")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}