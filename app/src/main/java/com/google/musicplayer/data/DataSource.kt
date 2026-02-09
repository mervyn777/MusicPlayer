package com.google.musicplayer.data

import com.google.musicplayer.R

object DataSource {
    val playlist = mutableListOf(
        SongTrack(
            id = "1",
            title = "Minding My Business",
            artist = "Gideon",
            album = null,
            albumArtRes = R.raw.gideon_business_art,
            trackRes = R.raw.gideon_business,
            duration = 340,
        ),
        SongTrack(
            id = "2",
            title = "Blessings Over Me",
            artist = "Natasha Chansa",
            album = null,
            albumArtRes = R.raw.natasha_blessings_art,
            trackRes = R.raw.natasha_blessings,
            duration = 736,
        ),
        SongTrack(
            id = "3",
            title = "Gba Gbogbo Iyanu",
            artist = "Nia Nnamdi",
            album = null,
            albumArtRes = R.raw.nia_iyanu_art,
            trackRes = R.raw.nia_iyanu,
            duration = 264,
        ),
        SongTrack(
            id = "4",
            title = "Escape From Love To Get Back To Love Again Much Stronger",
            artist = "Sidney Samson Eva Simons",
            album = "Love Remix Edition",
            albumArtRes = null,
            trackRes = R.raw.sidney_escape,
            duration = 427,
        ),
        SongTrack(
            id = "5",
            title = "Pillow Talking",
            artist = "Quincy",
            album = null,
            albumArtRes = R.raw.quincy_pillow_art,
            trackRes = R.raw.quincy_pillow,
            duration = 834,
        ),
        SongTrack(
            id = "6",
            title = "The Only Way Is Up Chaplin",
            artist = "Martin Garrix ft. Tiesto",
            album = null,
            albumArtRes = R.raw.tiesto_chaplin_art,
            trackRes = R.raw.tiesto_chaplin,
            duration = 145,
        ),
        SongTrack(
            id = "7",
            title = "Now I See",
            artist = "Gideon",
            album = null,
            albumArtRes = R.raw.gideon_see_art,
            trackRes = R.raw.gideon_see,
            duration = 736,
        ),
    )
}