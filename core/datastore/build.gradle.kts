plugins {
  alias(libs.plugins.multiplatform.library.compose)
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.multiplatformSettings)
    }

    sourceSets.androidMain.dependencies {
        implementation(libs.contextProvider)
    }
}
