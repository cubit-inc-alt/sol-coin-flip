plugins {
  alias(libs.plugins.feature)
  alias(libs.plugins.kotlinx.serilization)
}


kotlin {
  sourceSets.commonMain.dependencies {
    implementation(projects.core.resources)
  }
}
