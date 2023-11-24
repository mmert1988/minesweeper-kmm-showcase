package com.mehmedmert.minesweeperkmmshowcase.android.game

import com.mehmedmert.minesweeperkmmshowcase.domain.model.GameStatus

data class GameViewStatus(
    val gameStatus: GameStatus = GameStatus.Initial,
)
