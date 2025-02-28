package com.shivam_raj.circletalk.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

enum class Direction {
    LEFT, RIGHT
}

@Composable
fun BottomDesignedButton(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    designColor: Color,
    direction: Direction = Direction.LEFT,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.drawBehind {
            drawPath(getPath(size, direction), color = designColor)
        },
        contentAlignment = Alignment.BottomCenter
    ){
        TextButton(
            onClick = onClick,
            modifier = Modifier.navigationBarsPadding().padding(bottom = 8.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun getPath(size: Size, direction: Direction) : Path{
    val path = Path()
    return path.apply {
        if (direction == Direction.LEFT) {
            moveTo(0f, size.height * 0.85f)
            quadraticTo(
                size.width * 0.7f,
                size.height * 0.85f,
                size.width,
                size.height
            )
            lineTo(0f, size.height)
            close()
        } else {
            moveTo(size.width, size.height * 0.85f)
            quadraticTo(
                size.width * 0.3f,
                size.height * 0.85f,
                0f,
                size.height
            )
            lineTo(size.width, size.height)
            close()
        }
    }
}