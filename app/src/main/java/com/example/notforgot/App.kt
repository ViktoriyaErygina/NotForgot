package com.example.notforgot

import android.app.Application
import android.util.Log
import com.example.notforgot.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        Log.i(TAG, "startKoin: Start koin..")
        org.koin.core.context.startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        Log.i(TAG, "startKoin: Done..")
    }

    companion object {
        val TAG = App::class.java.simpleName
    }
}