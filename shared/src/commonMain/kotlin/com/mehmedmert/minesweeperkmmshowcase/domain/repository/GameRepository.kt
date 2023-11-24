package com.mehmedmert.minesweeperkmmshowcase.domain.repository

import com.mehmedmert.minesweeperkmmshowcase.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    val gameStatus: Flow<GameStatus>
    fun newGame(
        rows: Int = DEFAULT_ROWS,
        columns: Int = DEFAULT_COLUMNS,
        minesCount: Int = DEFAULT_MINES_COUNT,
    )
    suspend fun openCell(row: Int, column: Int)
    suspend fun toggleFlagCell(row: Int, column: Int)

    companion object {
        private const val DEFAULT_ROWS = 30
        private const val DEFAULT_COLUMNS = 30
        private const val DEFAULT_MINES_COUNT = 8
    }
}
