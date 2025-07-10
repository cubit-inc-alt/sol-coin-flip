import app.libs

plugins {
    alias(libs.plugins.multiplatform.library.compose)
    alias(libs.plugins.kotlinx.serilization)
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.common)
        implementation(projects.core.designSystem)
        implementation(projects.core.resources)
        implementation(projects.core.components.coil)
        implementation(libs.lottie.compose)
        implementation(projects.core.components.coil)
    }
}
