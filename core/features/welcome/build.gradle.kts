import org.gradle.kotlin.dsl.implementation

plugins {
  alias(libs.plugins.feature)
  alias(libs.plugins.kotlinx.serilization)
}


kotlin {
  sourceSets.commonMain.dependencies {
    implementation(projects.core.common)
    implementation(projects.core.features.connectWallet)

    implementation(projects.core.walletAdaptor)
    implementation(projects.core.web3)
  }
}
