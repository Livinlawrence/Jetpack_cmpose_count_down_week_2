/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.purple200
import com.example.androiddevchallenge.ui.theme.teal200

@Composable
fun CountDownScreen(
    timeInSec: Int,
    onCancel: () -> Unit
) {

    var trigger by remember { mutableStateOf(timeInSec) }

    val elapsed by animateIntAsState(
        targetValue = trigger * 1000,
        animationSpec = tween(timeInSec * 1000, easing = LinearEasing)
    )

    DisposableEffect(Unit) {
        trigger = 0
        onDispose { }
    }

    Column(
        Modifier
            .fillMaxHeight()
            .padding(start = 10.dp, end = 10.dp)
    ) {

        Box {
            AnimationElapsedTime(elapsed)
            AnimationCircleCanvas(elapsed)
        }

        Spacer(modifier = Modifier.size(55.dp))

        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(70.dp)
                .shadow(30.dp, shape = CircleShape)
                .clickable { onCancel() },
            imageVector = Icons.Default.Cancel,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Red)
        )
    }
}

@Composable
private fun BoxScope.AnimationElapsedTime(elapsed: Int) {

    val (hou, min, sec) = remember(elapsed / 1000) {
        val elapsedInSec = elapsed / 1000
        val hou = elapsedInSec / 3600
        val min = elapsedInSec / 60 - hou * 60
        val sec = elapsedInSec % 60
        Triple(hou, min, sec)
    }

    val mills = remember(elapsed) {
        elapsed % 1000
    }

    val onlySec = remember(hou, min) {
        hou == 0 && min == 0
    }

    val transition = rememberInfiniteTransition()

    val animatedFont by transition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse)
    )

    val (size, labelSize) = when {
        hou > 0 -> 40.sp to 20.sp
        min > 0 -> 80.sp to 30.sp
        else -> 150.sp to 50.sp
    }

    Row(
        Modifier
            .align(Alignment.Center)
            .padding(start = 55.dp, end = 55.dp, top = 10.dp, bottom = 10.dp)
    ) {
        if (hou > 0) {
            DisplayTime(
                hou.formatTime(),
                "h",
                fontSize = size,
                labelSize = labelSize
            )
        }
        if (min > 0) {
            DisplayTime(
                min.formatTime(),
                "m",
                fontSize = size,
                labelSize = labelSize
            )
        }
        DisplayTime(
            if (onlySec) sec.toString() else sec.formatTime(),
            if (onlySec) "" else "s",
            fontSize = size * (if (onlySec && sec < 30 && mills != 0) animatedFont else 1f),
            labelSize = labelSize,
            textAlign = if (onlySec) TextAlign.Center else TextAlign.End
        )
    }
}

@Composable
private fun BoxScope.AnimationCircleCanvas(durationMills: Int) {
    val transition = rememberInfiniteTransition()
    val trigger by remember { mutableStateOf(0f) }
    var isFinished by remember { mutableStateOf(false) }

    val animateTween by animateFloatAsState(
        targetValue = trigger,
        animationSpec = tween(
            durationMillis = durationMills,
            easing = LinearEasing
        ),
        finishedListener = {
            isFinished = true
        }
    )

    val animatedForward by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart)
    )
    val animatedReverse by transition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart)
    )
    val strokeRestart = Stroke(15f)
    val color = Color.Red
    Canvas(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(16.dp)
            .size(350.dp)
    ) {
        val diameter = size.minDimension
        val radius = diameter / 2f
        val topLeftOffset = Offset(10f, 10f)
        val size = Size(radius * 2, radius * 2)

        if (!isFinished) {
            drawArc(
                color = color,
                startAngle = animatedForward,
                sweepAngle = 150f,
                topLeft = topLeftOffset,
                size = size,
                useCenter = false,
                style = strokeRestart,
            )

            drawArc(
                color = Color.Blue,
                startAngle = animatedReverse,
                sweepAngle = 150f,
                topLeft = topLeftOffset,
                size = size,
                useCenter = false,
                style = strokeRestart,
            )
        }
        drawArc(
            startAngle = 0f,
            sweepAngle = animateTween,
            brush = Brush.radialGradient(
                radius = radius,
                colors = listOf(
                    purple200.copy(0.3f),
                    teal200.copy(0.2f),
                    Color.White.copy(0.3f)
                ),
            ),
            useCenter = true,
            style = Fill,
        )
    }
}

@Preview
@Composable
fun DisplayPreview() {
    CountDownScreen(30) {}
}

private fun Int.formatTime() = String.format("%02d", this)
