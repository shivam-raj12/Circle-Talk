package com.shivam_raj.circletalk.screens.auth

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin


@Composable
fun AnimatedHeader(
    text: String,
    textColor: Color,
    waveColor: Color
) {
    var textHeight by remember {
        mutableFloatStateOf(0f)
    }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    Box(modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
            val path = createWavePath(size, textHeight * 1.5f, waveOffset)
            drawPath(path, color = waveColor)
        }) {
        Text(
            text = text,
            style = MaterialTheme.typography.displaySmall,
            color = textColor,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 32.dp, bottom = 16.dp)
                .onSizeChanged {
                    textHeight = it.height.toFloat()
                },
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

private fun createWavePath(size: Size, height: Float, waveOffset: Float): Path {
    val path = Path()
    path.moveTo(0f, 0f)

    val waveAmplitude = height / 8f
    val waveFrequency = 1f

    for (x in 0..size.width.toInt() step 10) {
        val y = height + waveAmplitude * sin(
            2 * PI * waveFrequency * (x + waveOffset * size.width) / size.width
        )
        path.lineTo(x.toFloat(), y.toFloat())
    }

    path.lineTo(size.width, height)
    path.lineTo(size.width, 0f)
    path.close()
    return path
}