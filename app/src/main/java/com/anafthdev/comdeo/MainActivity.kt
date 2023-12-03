package com.anafthdev.comdeo

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.anafthdev.comdeo.foundation.theme.ComdeoTheme
import com.anafthdev.comdeo.ui.app.Comdeo
import com.anafthdev.comdeo.ui.app.ComdeoViewModel
import com.anafthdev.comdeo.util.VideoUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel: ComdeoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set system bar and navigation bar color to transparent
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ComdeoTheme {
                Comdeo(viewModel)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.insertVideo(VideoUtil.findAllVideo(this))
    }
}
