package com.example.heartogether

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.heartogether.services.ACTION_START_FOREGROUND_SERVICE
import com.example.heartogether.services.AudioService
import com.example.heartogether.ui.theme.HearTogetherTheme


class MainActivity : ComponentActivity() {

    private var mService: AudioService? = null
    private var mBound: Boolean = false


    //audio recorder service related
    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioService.AudioRecorderServiceBinder
            mService = binder.getService()
            Log.d("TAG", "onServiceConnected")
            mBound = true
        }


        override fun onServiceDisconnected(name: ComponentName?) {
            mService?.stopRecorderService()
            mBound = false
        }
    }
    override fun onStart() {
        super.onStart()
        bindAudioServiceWithPermission()
    }
    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mService?.stopRecorderService()
        mService = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                RequestPermission()
                HearTogetherTheme {
                    HearTogetherApp(
                        mService = mService
                    )
                }
            }
        }
    }

    @Composable
    fun RequestPermission() {
        val launcher: ManagedActivityResultLauncher<String, Boolean> =
            rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission Accepted: Do something
                    Log.d("TAG", "PERMISSION GRANTED")
                    startService(intent)
                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                }
            }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                LocalContext.current,
                Manifest.permission.RECORD_AUDIO
            ) -> {
                // Some works that require permission
                Log.d("TAG", "Code requires permission")
                startService(intent)
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
            else -> {
                // Asking for permission
                SideEffect {
                    launcher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun bindAudioServiceWithPermission() {
        val intent = Intent(this, AudioService::class.java)
        intent.action = ACTION_START_FOREGROUND_SERVICE
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 0)
                startService(intent)
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        } else {
            startService(intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

}

