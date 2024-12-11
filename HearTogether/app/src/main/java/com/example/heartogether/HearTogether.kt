package com.example.heartogether

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.heartogether.data.dictionary.DictionaryData
import com.example.heartogether.repository.DefaultMisPronunScreenRepo
import com.example.heartogether.repository.MisProNunRepo
import com.example.heartogether.repository.loadWordData

class HearTogether : Application() {
    lateinit var container: MisProNunRepo
    override fun onCreate() {
        super.onCreate()
        container = DefaultMisPronunScreenRepo()
        DictionaryData = loadWordData(this)
        Log.d("dawdawdawd", "${DictionaryData!!.size}")
    }
}