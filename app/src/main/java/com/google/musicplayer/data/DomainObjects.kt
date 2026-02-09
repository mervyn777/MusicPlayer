package com.google.musicplayer.data

import androidx.annotation.RawRes

data class SongTrack(
    val id: String,
    val title: String,
    val artist: String,
    val album: String?,
    @param:RawRes val albumArtRes: Int?,
    @param:RawRes val trackRes: Int,
    val duration: Long = 0,
    val isFavorite: Boolean = false,
)

data class PlayerState(
    val currentTrack: SongTrack?,
    val isPlaying: Boolean,
    val currentPosition: Long,
    val loopState: LoopState,
    val isFirstTrack: Boolean,
    val isLastTrack: Boolean,
    val isForwardDirection: Boolean,
)

enum class LoopState { OFF, ALL, ONE }