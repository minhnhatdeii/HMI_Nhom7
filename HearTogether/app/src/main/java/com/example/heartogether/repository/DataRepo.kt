package com.example.heartogether.repository

import android.content.Context
import android.util.Log
import com.example.heartogether.R
import com.example.heartogether.data.network.WordData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun loadWordData(context: Context): MutableList<WordData>? {
    Log.d("Tag","${R.raw.data}")
    val json = context.resources.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
    val type = object : TypeToken<List<WordData>>() {}.type
    return Gson().fromJson(json, type)
}

