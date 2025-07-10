import app.resourcesNamespace

plugins {
  alias(libs.plugins.multiplatform.library.compose)
}

kotlin {
  sourceSets.commonMain.dependencies {
    implementation(projects.core.resources)
    implementation(projects.core.designSystem)
    api(libs.coil.compose.core)
    api(libs.coil.compose)
    api(libs.coil.mp)
    api(libs.coil.network.ktor)
  }
}

compose.resources {
  packageOfResClass = resourcesNamespace
}
