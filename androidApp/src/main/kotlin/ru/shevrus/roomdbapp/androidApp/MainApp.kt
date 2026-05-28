package ru.shevrus.roomdbapp.androidApp

import android.app.Application
import ru.shevrus.roomdbapp.data.database.appContext
import ru.shevrus.roomdbapp.di.initKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        initKoin()
    }
}