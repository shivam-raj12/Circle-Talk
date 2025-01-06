package com.shivam_raj.circletalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shivam_raj.circletalk.navigation.NavGraph
import com.shivam_raj.circletalk.ui.theme.CircleTalkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CircleTalkTheme {
                NavGraph()
            }
        }
    }
}