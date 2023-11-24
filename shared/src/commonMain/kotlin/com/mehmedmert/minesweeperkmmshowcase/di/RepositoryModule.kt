package com.mehmedmert.minesweeperkmmshowcase.di

import com.mehmedmert.minesweeperkmmshowcase.domain.GameRepository
import com.mehmedmert.minesweeperkmmshowcase.repository.GameRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory { provideGameRepository() }
}

private fun provideGameRepository(): GameRepository = GameRepositoryImpl()
