package com.mehmedmert.minesweeperkmmshowcase.android.game

import kotlinx.datetime.Instant

data class GameViewStatus(
    val rows: Int = 0,
    val columns: Int = 0,
    val cells: List<Cell> = listOf(),
    val status: Status = Status.Initial,
    val startTime: Instant? = null,
    val remainingMines: Int? = null,
)

sealed interface Cell {
    data object Closed : Cell
    data class Open(val minesAroundCount: Int) : Cell
    data object Flagged : Cell
    data object Mined : Cell
}

enum class Status {
    Initial,
    Running,
    Won,
    Lost,
}