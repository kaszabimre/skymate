package io.kaszabimre.skymate.di

import io.kaszabimre.skymate.permission.LocationService
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformModule() = module {
    factory {
        Darwin.create()
    }
    single { LocationService() }
}
