package io.kaszabimre.skymate.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun networkModule(): Module

internal fun networkModule(isDebug: Boolean) = module {
    factory {
        HttpClient(engine = get()) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            Charsets {
                register(io.ktor.utils.io.charsets.Charsets.UTF_8)
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        explicitNulls = false
                    }
                )
            }

            if (isDebug) {
                install(Logging) {
                    level = LogLevel.ALL
                }
            }
        }
    }
}
