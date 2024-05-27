package org.example.project

import KoinInitializer
import android.app.Application

class KotlinApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}