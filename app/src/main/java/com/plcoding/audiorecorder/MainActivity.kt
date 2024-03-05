package com.plcoding.audiorecorder

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.plcoding.audiorecorder.playback.AndroidAudioPlayer
import com.plcoding.audiorecorder.record.AndroidAudioRecorder
import com.plcoding.audiorecorder.ui.theme.AudioRecorderTheme
import java.io.File

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }
    private var isInitializationComplete = false
    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { !isInitializationComplete }

        Handler(Looper.getMainLooper()).postDelayed({
            isInitializationComplete = true

            setContent {
                AudioRecorderTheme {
                    Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Start Recording Button with Icon
                            Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
                                Button(
                                    onClick = {
                                        File(cacheDir, "audio.mp3").also {
                                            recorder.start(it)
                                            audioFile = it
                                        }
                                    },
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    Icon(Icons.Filled.PlayArrow, contentDescription = "Start Recording")
                                    Text(text = "Start Recording", style = MaterialTheme.typography.button, modifier = Modifier.padding(start = 8.dp))
                                }
                            }

                            // Stop Recording Button with Icon
                            Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
                                Button(
                                    onClick = { recorder.stop() },
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    Icon(Icons.Filled.Done, contentDescription = "Stop Recording")
                                    Text(text = "Stop Recording", style = MaterialTheme.typography.button, modifier = Modifier.padding(start = 8.dp))
                                }
                            }

                            // Play Button with Icon
                            Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
                                Button(
                                    onClick = { player.playFile(audioFile ?: return@Button) },
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
                                    Text(text = "Play", style = MaterialTheme.typography.button, modifier = Modifier.padding(start = 8.dp))
                                }
                            }

                            // Stop Playing Button with Icon
                            Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
                                Button(
                                    onClick = { player.stop() },
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    Icon(Icons.Filled.Done, contentDescription = "Stop Playing")
                                    Text(text = "Stop Playing", style = MaterialTheme.typography.button, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }, 1700)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
    }
}
