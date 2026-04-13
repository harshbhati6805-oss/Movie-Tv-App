package com.harsh.tmdbtvapp.uii.home.component

import androidx.compose.foundation.focusable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*

@Composable
fun TvFocusableButton(
    text: String,
    isDefaultFocused: Boolean = false,
    focusRequester: FocusRequester? = null,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        modifier = Modifier
            .then(
                if (focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else Modifier
            )
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .focusable()
            .onPreviewKeyEvent { event ->   // 🔥 FIX ADDED
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.DirectionCenter || event.key == Key.Enter)
                ) {
                    onClick()
                    true
                } else {
                    false
                }
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFocused) Color.White else Color.DarkGray,
            contentColor = if (isFocused) Color.Black else Color.White
        )
    ) {
        Text(text)
    }

    // Request default focus
    LaunchedEffect(Unit) {
        if (isDefaultFocused && focusRequester != null) {
            focusRequester.requestFocus()
        }
    }
}