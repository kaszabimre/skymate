package io.kaszabimre.skymate.di

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual fun networkModule() = networkModule(isDebug = Platform.isDebugBinary)
