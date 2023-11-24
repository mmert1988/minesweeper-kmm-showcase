package com.mehmedmert.minesweeperkmmshowcase.android.game

import androidx.lifecycle.ViewModel
import com.mehmedmert.minesweeperkmmshowcase.domain.ConsumerAsyncUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.ConsumerUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.FlowSupplierUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GameViewModel(
    newGameUseCase: ConsumerUseCase<NewGameParams>,
    openCellUseCase: ConsumerAsyncUseCase<RowAndColumnParams>,
    toggleFlagUseCase: ConsumerAsyncUseCase<RowAndColumnParams>,
    gameStatusUseCase: FlowSupplierUseCase<GameStatus>,
) : ViewModel() {
    private val _gameViewStatus = flow<GameViewStatus> {  }

    val gameViewStatus: Flow<GameViewStatus> = _gameViewStatus

    fun onOpenCell(row: Int, column: Int) {
        // todo
    }

    fun onToggleFlag(row: Int, column: Int) {
        // todo
    }

    fun onStartNewGame() {
        // todo
    }
}

typealias NewGameParams = Triple<Int, Int, Int>
typealias RowAndColumnParams = Pair<Int, Int>
