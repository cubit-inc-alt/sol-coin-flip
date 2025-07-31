import app.libs

plugins {
  alias(libs.plugins.multiplatform.library.compose)
  alias(libs.plugins.kotlinx.serilization)
}

kotlin {
  sourceSets.commonMain.dependencies {
    implementation(projects.core.common)
    implementation(projects.core.analytics)
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.network)
    implementation(libs.jetbrain.navigation.compose)
    implementation(projects.core.components.coil)


    implementation(projects.core.features.welcome)
    implementation(projects.core.features.main)
    implementation(projects.core.walletAdaptor)
    implementation(libs.solana.rpc)
  }

}
