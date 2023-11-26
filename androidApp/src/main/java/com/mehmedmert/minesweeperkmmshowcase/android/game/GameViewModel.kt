package com.mehmedmert.minesweeperkmmshowcase.android.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehmedmert.minesweeperkmmshowcase.domain.ConsumerAsyncUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.ConsumerUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.FlowSupplierUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.model.GameStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GameViewModel(
    private val newGameUseCase: ConsumerUseCase<NewGameParams>,
    private val openCellUseCase: ConsumerAsyncUseCase<RowAndColumnParams>,
    private val toggleFlagUseCase: ConsumerAsyncUseCase<RowAndColumnParams>,
    gameStatusUseCase: FlowSupplierUseCase<GameStatus>,
    gameViewStatusMapper: GameViewStatusMapper,
) : ViewModel() {
    private val _gameViewStatus = gameStatusUseCase().map {
        gameViewStatusMapper.map(it)
    }

    val gameViewStatus: Flow<GameViewStatus> = _gameViewStatus

    fun onOpenCell(row: Int, column: Int) {
        viewModelScope.launch {
            openCellUseCase(Pair(row, column))
        }
    }

    fun onToggleFlag(row: Int, column: Int) {
        viewModelScope.launch {
            toggleFlagUseCase(Pair(row, column))
        }
    }

    fun onStartNewGame() {
        newGameUseCase(Triple(30, 30, 5))
    }
}

typealias NewGameParams = Triple<Int, Int, Int>
typealias RowAndColumnParams = Pair<Int, Int>
