package com.ugisozols.sqldelightcaching

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PersonsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}