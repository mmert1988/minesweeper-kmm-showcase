package com.mehmedmert.minesweeperkmmshowcase.domain.game

import com.mehmedmert.minesweeperkmmshowcase.domain.repository.GameRepository

class OpenCellUseCase(
    private val gameRepository: GameRepository,
) {
    suspend operator fun invoke(row: Int, column: Int) {
        gameRepository.openCell(row, column)
    }
}
