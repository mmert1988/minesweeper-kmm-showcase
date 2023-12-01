package com.mehmedmert.minesweeperkmmshowcase.di

import com.mehmedmert.minesweeperkmmshowcase.domain.repository.GameRepository
import com.mehmedmert.minesweeperkmmshowcase.repository.GameRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single { provideGameRepository() }
}

private fun provideGameRepository(): GameRepository = GameRepositoryImpl()
