import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    // trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.jetbrainsCompose).apply(false)
    alias(libs.plugins.detekt)
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension> {
        autoCorrect = true
        source.setFrom(
            objects.fileCollection().from(
                DetektExtension.DEFAULT_SRC_DIR_JAVA,
                DetektExtension.DEFAULT_TEST_SRC_DIR_JAVA,
                DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
                DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
                "src/androidMain",
                "src/commonMain",
                "src/iosMain",
                "src/commonTest",
                "src/iosTest",
                "src/androidUnitTest",
                "src/test",
                "src/testDebug",
                "src/testRelease",
                "build.gradle.kts",
            )
        )
        tasks.withType<Detekt>().configureEach {
            jvmTarget = "1.8"
            reports {
                html.required.set(true)
                xml.required.set(true)
            }
        }
        buildUponDefaultConfig = false
        config.setFrom(files("$rootDir/config/detekt.yml"))
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}
