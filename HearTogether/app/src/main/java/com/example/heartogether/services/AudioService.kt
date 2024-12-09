package com.example.heartogether.services


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.heartogether.R
import java.io.File
import java.io.IOException

//flags for using action received through notifications
const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
const val ACTION_START_RECORDING = "ACTION_START_RECORDING"
const val ACTION_STOP_RECORDING = "ACTION_STOP_RECORDING"


class AudioService : Service() {

    //Service binding
    private val audioRecorderServiceBinder = AudioRecorderServiceBinder()

    inner class AudioRecorderServiceBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    private var mediaRecorder: MediaRecorder? = null
    private var fileName: String? = null
    var isRecordingStarted: Boolean = false


    override fun onBind(intent: Intent?): IBinder {
        // Sử dụng externalCacheDir để lưu file tạm thời
        fileName = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            "${cacheDir.absolutePath}/recording_${System.currentTimeMillis()}.m4a"
        } else {
            "${externalCacheDir?.absolutePath}/recording_${System.currentTimeMillis()}.m4a"
        }
        return audioRecorderServiceBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TAG", "onStartCommand")

        when (intent?.action) {
            ACTION_START_FOREGROUND_SERVICE -> {
                startForegroundWithNotification()
            }
            ACTION_STOP_FOREGROUND_SERVICE -> {
                stopRecorderService()
            }
            ACTION_START_RECORDING -> {
                startRecording()
            }
            ACTION_STOP_RECORDING -> {
                stopRecording()
            }
            else -> {
                Log.d("TAG", "Unknown ${intent?.action}")
            }
        }
        return START_STICKY
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundWithNotification() {
        Log.d("TAG", "startForegroundWithNotification")
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else 0


        val stopIntent = Intent(this, AudioService::class.java)
        stopIntent.action = ACTION_STOP_FOREGROUND_SERVICE
        val stopPendingIntent =
            PendingIntent.getService(this, 123, stopIntent, flag)

        val stopRecordingIntent = Intent(this, AudioService::class.java)
        stopRecordingIntent.action = ACTION_STOP_RECORDING
        val stopRecordingPendingIntent =
            PendingIntent.getService(this, 123, stopRecordingIntent, flag)

        val startRecordingIntent = Intent(this, AudioService::class.java)
        startRecordingIntent.action = ACTION_START_RECORDING
        val startRecordingPendingIntent =
            PendingIntent.getService(this, 123, startRecordingIntent, flag)

        val pauseRecordingIntent = Intent(this, AudioService::class.java)
        pauseRecordingIntent.action = ACTION_STOP_FOREGROUND_SERVICE
        val pauseRecordingPendingIntent =
            PendingIntent.getService(this, 123, pauseRecordingIntent, flag)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("001", "Audio", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }


        val builder = NotificationCompat.Builder(
            this, "001",
        )
        builder.apply {
            setSmallIcon(R.mipmap.ic_launcher_round)
            setContentTitle("HearTogether")
            setContentText("Your Audio is being recorded in the background")
            addAction(NotificationCompat.Action(R.drawable.ic_stop, "STOP", stopPendingIntent))
            addAction(NotificationCompat.Action(0, "END", stopRecordingPendingIntent))
            addAction(NotificationCompat.Action(0, "RECORD", startRecordingPendingIntent))
            addAction(NotificationCompat.Action(0, "PLAY", pauseRecordingPendingIntent))
        }

        val notification = builder.build()

        startForeground(123, notification)
        Log.d("TAG", "startForegroundWithNotificationend")

    }

    private fun changeFieName() {
        fileName = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            "${cacheDir.absolutePath}/recording_${System.currentTimeMillis()}.m4a"
        } else {
            "${externalCacheDir?.absolutePath}/recording_${System.currentTimeMillis()}.m4a"
        }
    }

    //Audio recordings
    private fun initRecorder() {
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            @Suppress("deprecation")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(16 * 44100)
            setAudioSamplingRate(96000)
            setOutputFile(fileName)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e("TAG", "prepare() failed")
            }
        }
    }

    fun startRecording() {
        changeFieName()
        initRecorder()
        mediaRecorder?.start()
        isRecordingStarted = true
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        isRecordingStarted = false
        mediaRecorder = null
    }



    fun getRecordedFilePath(): String? {
        return fileName
    }

    fun stopRecorderService() {
        stopRecording()
        stopForeground(true)
        stopSelf()
    }
}