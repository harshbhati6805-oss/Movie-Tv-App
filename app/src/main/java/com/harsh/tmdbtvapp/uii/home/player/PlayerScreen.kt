package com.harsh.tmdbtvapp.uii.home.player

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import android.view.KeyEvent as AndroidKeyEvent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type

import kotlinx.coroutines.delay
import kotlin.coroutines.ContinuationInterceptor

// Netflix Yellow & Dim colors
private val NetflixYellow = Color(0xFFFFC107)
private val FocusedBg    = Color(0xFFFFC107)
private val UnfocusedBg  = Color(0x55FFFFFF)   // semi-transparent white circle

@Composable
fun PlayerScreen(
    videoUrl: String,
    movieTitle: String,
    navController: NavController
) {
    val context = LocalContext.current

    val decodedUrl = remember(videoUrl) {
        java.net.URLDecoder.decode(videoUrl, "UTF-8")
    }

    val player = remember { ExoPlayer.Builder(context).build() }

    var isPlaying       by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration        by remember { mutableStateOf(1L) }

    // Focus requesters
    val playFocusRequester    = remember { FocusRequester() }
    val rewindFocusRequester  = remember { FocusRequester() }
    val forwardFocusRequester = remember { FocusRequester() }

    // Auto-focus Play button on launch
    LaunchedEffect(Unit) {
        playFocusRequester.requestFocus()
    }

    LaunchedEffect(decodedUrl) {
        Log.d("PLAYER_URL", decodedUrl)
        player.setMediaItem(MediaItem.fromUri(decodedUrl))
        player.prepare()
        player.playWhenReady = true
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentPosition = player.currentPosition
            duration = if (player.duration > 0) player.duration else 1L
            delay(500)
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    BackHandler { navController.popBackStack() }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── VIDEO ──────────────────────────────────────────────
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = player
                    useController = false
                    isFocusable = false
                    isFocusableInTouchMode = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // ── TITLE ──────────────────────────────────────────────
        Text(
            text = movieTitle,
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 24.dp)
        )

        // ── BOTTOM CONTROLS ────────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            // ── PROGRESS BAR with overflow thumb ─────────────────────────────
            var isProgressFocused by remember { mutableStateOf(false) }
            val progressFocusRequester = remember { FocusRequester() }

            val thumbSize = 18.dp
            val trackHeight = if (isProgressFocused) 6.dp else 4.dp
            val thumbFraction = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f
            val progressColor = if (isProgressFocused) NetflixYellow else Color.White

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(thumbSize)                          // full height = thumb size so dot is not clipped
                    .focusRequester(progressFocusRequester)
                    .onFocusChanged { isProgressFocused = it.isFocused }
                    .focusable()
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown) {
                            when (keyEvent.nativeKeyEvent.keyCode) {
                                AndroidKeyEvent.KEYCODE_DPAD_RIGHT -> {
                                    player.seekTo(currentPosition + 10_000)
                                    true
                                }
                                AndroidKeyEvent.KEYCODE_DPAD_LEFT -> {
                                    player.seekTo(currentPosition - 10_000)
                                    true
                                }
                                else -> false
                            }
                        } else false
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                // ── TRACK (inactive) ─────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White.copy(alpha = 0.4f))
                )

                // ── TRACK (active) ───────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth(thumbFraction)
                        .height(trackHeight)
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(3.dp))
                        .background(progressColor)
                )

// ── THUMB DOT ────────────────────────────────────────────────────
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val width = maxWidth

                    Box(
                        modifier = Modifier
                            .offset(x = width * thumbFraction - thumbSize / 2)
                            .size(thumbSize)
                            .clip(CircleShape)
                            .background(progressColor)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }

            // ── TIMESTAMPS ───────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatTime(currentPosition), color = Color.White, fontSize = 13.sp)
                Text(formatTime(duration),        color = Color.White, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── PLAYBACK BUTTONS ─────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // REWIND
                NetflixIconButton(
                    focusRequester = rewindFocusRequester,
                    size = 44,
                    onClick = { player.seekBack() }
                ) { isFocused ->
                    Icon(
                        Icons.Default.FastRewind,
                        contentDescription = "Rewind",
                        tint = if (isFocused) Color.Black else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))

                // PLAY / PAUSE  (bigger circle)
                NetflixIconButton(
                    focusRequester = playFocusRequester,
                    size = 56,
                    onClick = {
                        isPlaying = !isPlaying
                        player.playWhenReady = isPlaying
                    }
                ) { isFocused ->
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = if (isFocused) Color.Black else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))

                // FORWARD
                NetflixIconButton(
                    focusRequester = forwardFocusRequester,
                    size = 44,
                    onClick = { player.seekForward() }
                ) { isFocused ->
                    Icon(
                        Icons.Default.FastForward,
                        contentDescription = "Forward",
                        tint = if (isFocused) Color.Black else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

// ── REUSABLE NETFLIX-STYLE CIRCLE BUTTON ───────────────────────────
@Composable
private fun NetflixIconButton(
    focusRequester: FocusRequester,
    size: Int,
    onClick: () -> Unit,
    content: @Composable (isFocused: Boolean) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(if (isFocused) FocusedBg else UnfocusedBg)
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) NetflixYellow else Color.Transparent,
                shape = CircleShape
            )
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.nativeKeyEvent.keyCode == AndroidKeyEvent.KEYCODE_DPAD_CENTER ||
                            event.nativeKeyEvent.keyCode == AndroidKeyEvent.KEYCODE_ENTER)
                ) {
                    onClick()
                    true
                } else false
            }
    ) {
        content(isFocused)
    }
}

// ── TIME FORMAT ────────────────────────────────────────────────────
fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}