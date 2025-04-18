package io.kaszabimre.skymate.di

import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

internal actual fun platformModule() = module {
    single {
        OkHttp.create()
    }
}
