package com.mehmedmert.minesweeperkmmshowcase.domain.game

import com.mehmedmert.minesweeperkmmshowcase.domain.repository.GameRepository

class NewGameUseCase(
    private val gameRepository: GameRepository,
) {
    operator fun invoke(rows: Int, columns: Int, mines: Int) {
        gameRepository.newGame(rows, columns, mines)
    }
}
