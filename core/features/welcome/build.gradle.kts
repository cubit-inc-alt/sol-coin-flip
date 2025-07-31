import org.gradle.kotlin.dsl.implementation

plugins {
  alias(libs.plugins.feature)
  alias(libs.plugins.kotlinx.serilization)
}


kotlin {
  sourceSets.commonMain.dependencies {
    implementation(libs.solana)
    implementation(libs.solana.rpc)
    implementation(libs.solana.eddsa)
    implementation(projects.core.sol)
    implementation(projects.core.features.connectWallet)
    implementation(projects.core.resources)
  }
}
