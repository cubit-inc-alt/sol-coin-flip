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
    implementation(libs.jetbrain.navigation.compose)
    implementation(projects.core.components.coil)


    implementation(projects.core.features.welcome)
    implementation(projects.core.walletAdaptor)

    implementation(projects.core.walletAdaptor)
    implementation(libs.solana)
    implementation(libs.solana.rpc)
    implementation(libs.solana.base58)
  }

}
