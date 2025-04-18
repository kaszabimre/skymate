package io.kaszabimre.skymate.di

import io.kaszabimre.skymate.BuildConfig

internal actual fun networkModule() = networkModule(isDebug = BuildConfig.DEBUG)
