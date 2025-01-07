package com.shivam_raj.circletalk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.shivam_raj.circletalk.navigation.NavGraph
import com.shivam_raj.circletalk.ui.theme.CircleTalkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var showSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            showSplash
        }
        enableEdgeToEdge()
        setContent {
            CircleTalkTheme {
                NavGraph {
                    showSplash = false
                    Log.d("TAG", "onCreate: ")
                }
            }
        }
    }
}