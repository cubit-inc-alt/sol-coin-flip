plugins {
    alias(libs.plugins.multiplatform.library.compose)
    alias(libs.plugins.config)
}

kotlin{
    sourceSets.commonMain.dependencies {
        implementation(projects.core.resources)
    }
}
