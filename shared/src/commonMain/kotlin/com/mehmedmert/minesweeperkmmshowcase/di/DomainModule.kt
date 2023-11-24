package com.mehmedmert.minesweeperkmmshowcase.di

import com.mehmedmert.minesweeperkmmshowcase.domain.game.NewGameUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.game.OpenCellUseCase
import com.mehmedmert.minesweeperkmmshowcase.domain.game.ToggleFlagUseCase
import org.koin.core.scope.Scope
import org.koin.dsl.module

val domainModule = module {
    factory { provideNewGameUseCase() }

    factory { provideOpenCellUseCase() }

    factory { provideToggleFlagUseCase() }
}

private fun Scope.provideNewGameUseCase() = NewGameUseCase(
    gameRepository = get(),
)

private fun Scope.provideOpenCellUseCase() = OpenCellUseCase(
    gameRepository = get(),
)

private fun Scope.provideToggleFlagUseCase() = ToggleFlagUseCase(
    gameRepository = get(),
)
