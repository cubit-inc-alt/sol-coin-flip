plugins {
    alias(libs.plugins.multiplatform.library.compose)
    alias(libs.plugins.kotlinx.serilization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.models)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
