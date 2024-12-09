package com.example.heartogether

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.heartogether.repository.DefaultMisPronunScreenRepo
import com.example.heartogether.repository.MisProNunRepo

class HearTogether : Application() {
    lateinit var container: MisProNunRepo
    override fun onCreate() {
        super.onCreate()
        container = DefaultMisPronunScreenRepo()
    }
}