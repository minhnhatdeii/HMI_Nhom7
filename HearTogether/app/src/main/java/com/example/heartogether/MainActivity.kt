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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.heartogether.services.ACTION_START_FOREGROUND_SERVICE
import com.example.heartogether.services.AudioService
import com.example.heartogether.ui.account.LoginScreen
import com.example.heartogether.ui.home.MispronounceScreen
import com.example.heartogether.ui.theme.HearTogetherTheme

class MainActivity : ComponentActivity() {

    private var mService: AudioService? = null
    private val _mBound = MutableLiveData<Boolean>(false)
    val mBound: LiveData<Boolean> get() = _mBound

    //audio recorder service related
    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioService.AudioRecorderServiceBinder
            mService = binder.getService()
            Log.d("TAG", "onServiceConnected")
            Log.d("TAG", "${mService}")

            _mBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("TAG", "onServiceDisconnected")
            mService?.stopRecorderService()
            _mBound.value = false
        }
    }

    override fun onStart() {
        super.onStart()
        bindAudioServiceWithPermission()
    }

    override fun onStop() {
        super.onStop()
        Log.d("TAG", "Stop")
        unbindService(connection)
        mService?.stopRecorderService()
        mService = null
        _mBound.value = false
    }

    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HearTogetherTheme {
                RequestPermission()
                Log.d("TAG", "${mService}")
                val isServiceBound = mBound.observeAsState(initial = false)
                if (isServiceBound.value) {
                    HearTogetherApp(mService = mService)
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
        Log.d("TBG","bindAudioServiceWithPermission")
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

