package io.kaszabimre.skymate.android

import android.app.Application
import io.kaszabimre.skymate.di.initDi

class SkymateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDi(applicationContext)
    }
}
