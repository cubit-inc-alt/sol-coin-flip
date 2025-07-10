plugins {
    alias(libs.plugins.multiplatform.library)
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.napier)
    }
}
