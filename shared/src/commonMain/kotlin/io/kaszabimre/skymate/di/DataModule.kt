package io.kaszabimre.skymate.di

import io.kaszabimre.skymate.domain.WeatherAction
import io.kaszabimre.skymate.domain.WeatherService
import io.kaszabimre.skymate.domain.WeatherStore
import io.kaszabimre.skymate.network.WeatherApi
import io.kaszabimre.skymate.network.WeatherApiImpl
import org.koin.core.parameter.parametersOf
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {
    single {
        WeatherService(
            api = get(),
            logger = get { parametersOf(WeatherService::class.simpleName) }
        )
    } binds arrayOf(WeatherAction::class, WeatherStore::class)
}

var apiModule = module {
    single<WeatherApi> { WeatherApiImpl(get()) }
}
