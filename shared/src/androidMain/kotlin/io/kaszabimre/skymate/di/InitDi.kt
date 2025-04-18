package io.kaszabimre.skymate.di

import android.content.Context
import org.koin.dsl.module

fun initDi(context: Context) {
    initKoin(
        appModule = module {
            single<Context> { context }
        }
    )
}
