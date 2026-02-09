package com.google.musicplayer.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.musicplayer.R
import com.google.musicplayer.data.LoopState
import com.google.musicplayer.data.PlayerState
import com.google.musicplayer.data.SongTrack
import com.google.musicplayer.ui.theme.BackgroundColor
import com.google.musicplayer.ui.theme.HeartColor
import com.google.musicplayer.ui.theme.PlayPauseColor
import com.google.musicplayer.ui.theme.TimeStampTextColor
import com.google.musicplayer.ui.theme.googleSansFamily

@Composable
fun EmbeddedMusicPlayer(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onSeekBarChanged: (Long) -> Unit,
    onLoopClicked: () -> Unit,
    onSkipPreviousClicked: () -> Unit,
    onPlayPauseClicked: () -> Unit,
    onSkipNextClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
) {
    var isBlurredBackground by remember { mutableStateOf(false) }

    PlayerBackground(
        modifier = modifier.fillMaxWidth(),
        playerState = playerState,
        isBlurred = isBlurredBackground
    ) {
        Column(
            modifier = modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SongTrackInfo(playerState) { isBlurredBackground = !isBlurredBackground }

            Spacer(modifier = Modifier.height(8.dp))

            SongTrackSeekbar(playerState = playerState, onSeekBarChanged = onSeekBarChanged)

            Spacer(modifier = Modifier.height(4.dp))

            PlayBackControls(
                playerState = playerState,
                onLoopClicked = onLoopClicked,
                onSkipPreviousClicked = onSkipPreviousClicked,
                onPlayPauseClicked = onPlayPauseClicked,
                onSkipNextClicked = onSkipNextClicked,
                onFavoriteClicked = onFavoriteClicked
            )
        }
    }
}

@SuppressLint("ResourceType")
@Composable
private fun PlayerBackground(
    modifier: Modifier,
    playerState: PlayerState,
    isBlurred: Boolean,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.clip(RoundedCornerShape(16.dp))) {
        if (isBlurred) {
            Image(
                painter = painterResource(
                    playerState.currentTrack?.albumArtRes ?: R.raw.default_art
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                alignment = Alignment.Center,
                modifier = modifier
                    .matchParentSize()
                    .blur(radius = 85.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            )
            Box(
                modifier = modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.15f))
            )
        } else {
            Box(
                modifier = modifier
                    .matchParentSize()
                    .background(BackgroundColor)
            )
        }
        content()
    }
}

@Composable
@SuppressLint("UnusedContentLambdaTargetStateParameter")
private fun SongTrackInfo(
    playerState: PlayerState, onBlurBackgroundChange: () -> Unit
) {
    val songTrack = playerState.currentTrack ?: SongTrack(
        id = "0",
        title = "No track selected",
        artist = "",
        album = null,
        albumArtRes = null,
        trackRes = 0,
    )
    AnimatedContent(
        targetState = songTrack.title, transitionSpec = {
            val forward = if (playerState.isForwardDirection) 1 else -1
            ContentTransform(targetContentEnter = slideInHorizontally { fullWidth -> fullWidth * forward } + fadeIn(
                animationSpec = tween(300)
            ),
                initialContentExit = slideOutHorizontally { fullWidth -> fullWidth * -forward } + fadeOut(
                    animationSpec = tween(300)
                ),
                sizeTransform = SizeTransform(clip = false))
        }, label = "TrackInfoAnimation"
    ) { _ ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Album Art
            Image(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(onDoubleTap = {
                            onBlurBackgroundChange()
                        })
                    }, painter = painterResource(
                    id = songTrack.albumArtRes ?: android.R.drawable.ic_menu_gallery
                ), contentScale = ContentScale.Crop, contentDescription = "Album Art"
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Track Title
                Text(
                    text = songTrack.title,
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        fontFamily = googleSansFamily,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Track Artist + Album
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = "${songTrack.artist}${if (songTrack.album != null) " • ${songTrack.album}" else ""}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = googleSansFamily,
                        fontWeight = FontWeight(400),
                        color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongTrackSeekbar(
    playerState: PlayerState, onSeekBarChanged: (Long) -> Unit
) {
    val isEmptyState = playerState.currentTrack == null
    Slider(
        modifier = Modifier.fillMaxWidth(),
        value = playerState.currentPosition.toFloat(),
        thumb = {
            Box(modifier = Modifier.size(20.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(if (isEmptyState) 0.dp else 12.dp)
                        .background(MaterialTheme.colorScheme.inversePrimary, CircleShape)
                )
            }
        },
        track = { sliderState ->
            SliderDefaults.Track(
                sliderState = sliderState,
                modifier = Modifier.height(3.dp),
                thumbTrackGapSize = 0.dp,
                drawStopIndicator = null,
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.inversePrimary,
                    inactiveTrackColor = MaterialTheme.colorScheme.outline
                )
            )
        },
        onValueChange = { onSeekBarChanged(it.toLong()) },
        valueRange = 0f..(playerState.currentTrack?.duration ?: 0).toFloat(),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-8).dp)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SeekerTimeStamp(timeInSeconds = playerState.currentPosition, isEmptyState = isEmptyState)
        SeekerTimeStamp(
            timeInSeconds = playerState.currentTrack?.duration ?: 0, isEmptyState = isEmptyState
        )
    }
}

@Composable
private fun SeekerTimeStamp(timeInSeconds: Long, isEmptyState: Boolean = false) {
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60
    Text(
        text = "$minutes:${seconds.toString().padStart(2, '0')}",
        style = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = googleSansFamily,
            fontWeight = FontWeight(500),
            color = if (isEmptyState) Color.Gray else TimeStampTextColor,
            letterSpacing = 0.1.sp
        ),
    )
}

@Composable
private fun PlayBackControls(
    playerState: PlayerState,
    onLoopClicked: () -> Unit,
    onSkipPreviousClicked: () -> Unit,
    onPlayPauseClicked: () -> Unit,
    onSkipNextClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
) {
    val isEmptyState = playerState.currentTrack == null
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val enabledColor = MaterialTheme.colorScheme.inverseOnSurface
        val disabledColor = Color.Gray
        IconButton(
            modifier = Modifier.size(36.dp), enabled = !isEmptyState, onClick = onLoopClicked
        ) {
            Icon(
                imageVector = playerState.loopState.icon,
                tint = if (playerState.loopState != LoopState.OFF && !isEmptyState) enabledColor else disabledColor,
                contentDescription = "Loop options: ${playerState.loopState.name}"
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        val disablePrevButton =
            playerState.loopState == LoopState.OFF && playerState.isFirstTrack || isEmptyState
        IconButton(
            modifier = Modifier.size(36.dp),
            enabled = !disablePrevButton,
            onClick = onSkipPreviousClicked
        ) {
            Icon(
                imageVector = Icons.Filled.SkipPrevious,
                tint = if (disablePrevButton) disabledColor else enabledColor,
                contentDescription = "Previous"
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        IconButton(
            modifier = Modifier
                .size(72.dp)
                .background(color = PlayPauseColor, shape = CircleShape),
            enabled = !isEmptyState,
            onClick = onPlayPauseClicked
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = if (playerState.isPlaying && !isEmptyState) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                tint = if (isEmptyState) disabledColor else enabledColor,
                contentDescription = if (playerState.isPlaying) "Pause" else "Play"
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        val disableNextButton =
            playerState.loopState == LoopState.OFF && playerState.isLastTrack || isEmptyState
        IconButton(
            modifier = Modifier.size(36.dp),
            enabled = !disableNextButton,
            onClick = onSkipNextClicked
        ) {
            Icon(
                Icons.Filled.SkipNext,
                tint = if (disableNextButton) disabledColor else enabledColor,
                contentDescription = "Next"
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        IconButton(
            modifier = Modifier.size(36.dp), enabled = !isEmptyState, onClick = onFavoriteClicked
        ) {
            Icon(
                imageVector = if (playerState.currentTrack?.isFavorite == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                tint = if (playerState.currentTrack?.isFavorite == true && !isEmptyState) HeartColor else disabledColor,
                contentDescription = if (playerState.currentTrack?.isFavorite == true) "Unfavorite" else "Favorite",
            )
        }
    }
}

private val LoopState.icon: ImageVector
    get() = when (this) {
        LoopState.OFF -> Icons.Filled.Repeat
        LoopState.ALL -> Icons.Filled.Repeat
        LoopState.ONE -> Icons.Filled.RepeatOne
    }