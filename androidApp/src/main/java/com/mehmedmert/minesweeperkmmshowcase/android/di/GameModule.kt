package com.mehmedmert.minesweeperkmmshowcase.android.di

import com.mehmedmert.minesweeperkmmshowcase.android.game.GameViewModel
import com.mehmedmert.minesweeperkmmshowcase.domain.game.NewGameUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.game.OpenCellUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.game.ToggleFlagUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.repository.GameRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

val gameModule = module {
    viewModel { provideGameViewModel() }
}

private fun Scope.provideGameViewModel() = GameViewModel(
    newGameUseCase = { get<NewGameUseCase>().invoke(it.first, it.second, it.third) },
    openCellUseCase = { get<OpenCellUseCase>().invoke(it.first, it.second) },
    toggleFlagUseCase = { get<ToggleFlagUseCase>().invoke(it.first, it.second) },
    gameStatusUseCase = get<GameRepository>()::gameStatus
)
