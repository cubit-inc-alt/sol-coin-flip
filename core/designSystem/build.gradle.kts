import app.namespace
import app.resourcesNamespace


plugins {
  alias(libs.plugins.multiplatform.library.compose)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.common)
      implementation(projects.core.resources)
    }
  }
}

compose.resources {
  packageOfResClass = resourcesNamespace
}

android {
  androidResources {
    this.noCompress.addAll(listOf())
  }
}
