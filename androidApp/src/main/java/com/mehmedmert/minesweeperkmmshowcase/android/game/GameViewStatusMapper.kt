package com.mehmedmert.minesweeperkmmshowcase.android.game

import com.mehmedmert.minesweeperkmmshowcase.domain.model.GameStatus

class GameViewStatusMapper {
    fun map(gameStatus: GameStatus) = GameViewStatus(
        rows = gameStatus.rows,
        columns = gameStatus.columns,
        cells = mapCells(gameStatus),
        status = when (gameStatus) {
            is GameStatus.Running -> Status.Running
            is GameStatus.Won -> Status.Won
            is GameStatus.Lost -> Status.Lost
            else -> Status.Initial
        },
        startTime = when (gameStatus) {
            is GameStatus.Running -> gameStatus.startTime
            else -> null
        },
        remainingMines = when (gameStatus) {
            is GameStatus.Running -> gameStatus.remainingMines
            else -> null
        },
    )

    private fun mapCells(gameStatus: GameStatus): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (row in 0..<gameStatus.rows) {
            for (column in 0..<gameStatus.columns) {
                val cellCoordinates = Pair(row, column)
                val cell = when {
                    gameStatus.flags.contains(cellCoordinates) -> Cell.Flagged
                    gameStatus.openedCells.keys.contains(cellCoordinates) -> {
                        gameStatus.openedCells[cellCoordinates]?.let { minesAroundCount ->
                            Cell.Open(minesAroundCount)
                        } ?: Cell.Closed
                    }

                    gameStatus is GameStatus.Lost && gameStatus.mines.contains(cellCoordinates) -> Cell.Mined
                    else -> Cell.Closed
                }
                cells.add(cell)
            }
        }
        return cells
    }
}
