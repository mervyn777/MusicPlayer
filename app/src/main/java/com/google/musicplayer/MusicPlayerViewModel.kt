package com.google.musicplayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.musicplayer.data.DataSource.playlist
import com.google.musicplayer.data.LoopState
import com.google.musicplayer.data.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor() : ViewModel() {

    private var currentSongTrackIndex = 0

    var playerState by mutableStateOf(
        PlayerState(
//            currentTrack = null, //Empty State
            currentTrack = playlist[currentSongTrackIndex],
            isPlaying = false,
            currentPosition = 0,
            loopState = LoopState.OFF,
            isFirstTrack = true,
            isLastTrack = false,
            isForwardDirection = true,
        )
    )
        private set

    fun onSeekBarChanged(position: Long) {
        playerState = playerState.copy(currentPosition = position)
    }

    fun changeLoop() {
        val newLoop = when (playerState.loopState) {
            LoopState.OFF -> LoopState.ALL
            LoopState.ALL -> LoopState.ONE
            LoopState.ONE -> LoopState.OFF
        }
        playerState = playerState.copy(loopState = newLoop)
    }

    fun playPreviousTrack() {
        when (playerState.loopState) {
            LoopState.OFF -> if (currentSongTrackIndex > 0) {
                currentSongTrackIndex--
            }

            LoopState.ALL -> currentSongTrackIndex =
                (currentSongTrackIndex - 1 + playlist.size) % playlist.size

            LoopState.ONE -> Unit
        }
        updateTrack(false)
    }

    fun playPauseTrack() {
        playerState = playerState.copy(isPlaying = !playerState.isPlaying)
    }

    fun playNextTrack() {
        when (playerState.loopState) {
            LoopState.OFF -> if (currentSongTrackIndex < playlist.size - 1) {
                currentSongTrackIndex++
            }

            LoopState.ALL -> currentSongTrackIndex = (currentSongTrackIndex + 1) % playlist.size
            LoopState.ONE -> Unit
        }
        updateTrack(true)
    }

    fun likeUnlikeTrack() {
        val songTrack = playlist[currentSongTrackIndex]
        val updatedSongTrack = songTrack.copy(isFavorite = !songTrack.isFavorite)
        playlist[currentSongTrackIndex] = updatedSongTrack
        playerState = playerState.copy(currentTrack = playlist[currentSongTrackIndex])
    }

    private fun updateTrack(isForwardDirection: Boolean) {
        playerState = playerState.copy(
            currentTrack = playlist[currentSongTrackIndex],
            currentPosition = 0,
            isFirstTrack = currentSongTrackIndex == 0,
            isLastTrack = currentSongTrackIndex == playlist.size - 1,
            isForwardDirection = isForwardDirection
        )
    }

}