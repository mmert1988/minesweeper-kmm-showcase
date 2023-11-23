package com.mehmedmert.minesweeperkmmshowcase.domain

import kotlinx.datetime.Instant

sealed class GameStatus(
    open val rows: Int,
    open val columns: Int,
    open val openedCells: Map<Pair<Int, Int>, Int>,
    open val flags: Set<Pair<Int, Int>>,
) {
    data class Running(
        override val rows: Int,
        override val columns: Int,
        override val openedCells: Map<Pair<Int, Int>, Int>,
        override val flags: Set<Pair<Int, Int>>,
        val startTime: Instant,
        val remainingMines: Int,
    ) : GameStatus(rows, columns, openedCells, flags)

    data class Won(
        override val rows: Int,
        override val columns: Int,
        override val openedCells: Map<Pair<Int, Int>, Int>,
        override val flags: Set<Pair<Int, Int>>,
    ) : GameStatus(rows, columns, openedCells, flags)

    data class Lost(
        override val rows: Int,
        override val columns: Int,
        override val openedCells: Map<Pair<Int, Int>, Int>,
        override val flags: Set<Pair<Int, Int>>,
        val mines: List<Pair<Int, Int>>,
    ) : GameStatus(rows, columns, openedCells, flags)
}
