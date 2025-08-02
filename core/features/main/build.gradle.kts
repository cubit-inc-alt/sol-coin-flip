plugins {
  alias(libs.plugins.feature)
  alias(libs.plugins.kotlinx.serilization)
}


kotlin {
  sourceSets.commonMain.dependencies {
    implementation(projects.core.resources)
    implementation(projects.core.network)

    implementation(projects.core.walletAdaptor)
    implementation(projects.core.web3)
  }
}
