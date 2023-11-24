package com.mehmedmert.minesweeperkmmshowcase.android

import android.app.Application
import com.mehmedmert.minesweeperkmmshowcase.android.di.androidModule
import com.mehmedmert.minesweeperkmmshowcase.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            androidLogger()
            modules(androidModule() + appModule())
        }
    }
}
