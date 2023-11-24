package com.mehmedmert.minesweeperkmmshowcase.domain.game

import com.mehmedmert.minesweeperkmmshowcase.domain.repository.GameRepository

class ToggleFlagUseCase(
    private val gameRepository: GameRepository,
) {
    suspend operator fun invoke(row: Int, column: Int) {
        gameRepository.toggleFlagCell(row, column)
    }
}
