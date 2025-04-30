package com.example.notforgot

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.notforgot.di.appModule
import com.example.notforgot.domain.models.NotificationConstants.NOTIFICATION_CHANNEL_ID
import com.example.notforgot.domain.models.NotificationConstants.NOTIFICATION_CHANNEL_NAME
import com.example.notforgot.domain.notification.AlarmReceiver
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import java.util.Calendar

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin()
        createSyncNotificationChannel()
    }

    private fun startKoin() {
        Log.i(TAG, "startKoin: Start koin..")
        org.koin.core.context.startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        Log.i(TAG, "startKoin: Done..")
    }

    private fun startNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        }
    }

    private fun createSyncNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }

    companion object {
        private val TAG = App::class.java.simpleName
    }
}