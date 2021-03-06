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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.typography

@Composable
fun StartingPoint(onStart: (it: Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            "00:30",
            Modifier
                .padding(bottom = 20.dp, top = 255.dp),
            fontSize = 30.sp,
            fontFamily = FontFamily.Monospace,
            style = typography.subtitle1,
            color = Color.Red,
        )

        Image(
            modifier = Modifier
                .size(70.dp)
                .shadow(30.dp, shape = CircleShape)
                .clickable {
                    onStart(
                        (30)
                    )
                },
            imageVector = Icons.Default.PlayCircle,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Red)
        )
    }
}

@Composable
fun RowScope.DisplayTime(
    num: String,
    label: String = "",
    fontSize: TextUnit = displayFontSize,
    labelSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign = TextAlign.End
) {
    val textColor = Color.Red

    Text(
        num,
        Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        textAlign = textAlign,
        fontSize = fontSize,
        fontFamily = FontFamily.Monospace,
        color = textColor,
        style = typography.subtitle1
    )
    if (label.isNotEmpty()) {
        Text(
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterVertically),
            text = label,
            fontSize = labelSize,
            color = textColor
        )
        Spacer(modifier = Modifier.width(15.dp))
    }
}

val displayFontSize = 50.sp
