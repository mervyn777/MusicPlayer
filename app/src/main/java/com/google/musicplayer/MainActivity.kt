package com.google.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.musicplayer.ui.component.EmbeddedMusicPlayer
import com.google.musicplayer.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MusicPlayerViewModel by viewModels<MusicPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                MusicPlayerScreen(viewModel)
            }
        }
    }
}

@Composable
fun MusicPlayerScreen(viewModel: MusicPlayerViewModel) {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            EmbeddedMusicPlayer(
                playerState = viewModel.playerState,
                onSeekBarChanged = viewModel::onSeekBarChanged,
                onLoopClicked = viewModel::changeLoop,
                onSkipPreviousClicked = viewModel::playPreviousTrack,
                onPlayPauseClicked = viewModel::playPauseTrack,
                onSkipNextClicked = viewModel::playNextTrack,
                onFavoriteClicked = viewModel::likeUnlikeTrack
            )
        }
    }
}