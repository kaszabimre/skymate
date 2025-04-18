package io.kaszabimre.skymate.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

internal fun initKoin(appModule: Module): KoinApplication = startKoin {
    modules(
        appModule,
        apiModule,
        networkModule(),
        factoryModule,
        viewModelModule,
        dataModule,
        platformModule()
    )
}
