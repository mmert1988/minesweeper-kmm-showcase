package com.mehmedmert.minesweeperkmmshowcase.domain

import kotlinx.coroutines.flow.Flow

typealias UseCase<P, R> = (P) -> R

typealias AsyncUseCase<P, R> = suspend (P) -> R

typealias ConsumerUseCase<P> = UseCase<P, Unit>

typealias ConsumerAsyncUseCase<P> = AsyncUseCase<P, Unit>

typealias SupplierUseCase<R> = () -> R

typealias FlowSupplierUseCase<R> = SupplierUseCase<Flow<R>>
