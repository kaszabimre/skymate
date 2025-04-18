package io.kaszabimre.skymate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform