package io.kaszabimre.skymate.di

import io.kaszabimre.skymate.viewmodel.HomeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { HomeViewModel(get()) }
}
