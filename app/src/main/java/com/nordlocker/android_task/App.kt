package com.nordlocker.android_task

import android.app.Application
import com.nordlocker.android_task.di.appModule
import com.nordlocker.android_task.util.TimberLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(
                Timber.DebugTree()
            )
        }

        startKoin {
            if (BuildConfig.DEBUG) {
                logger(TimberLogger())
            }
            androidContext(this@App)
            modules(appModule)
        }
    }
}