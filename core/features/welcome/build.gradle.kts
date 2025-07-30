import org.gradle.kotlin.dsl.implementation

plugins {
  alias(libs.plugins.multiplatform.library.compose)
  alias(libs.plugins.kotlinx.serilization)
}


kotlin{
  sourceSets.commonMain.dependencies {
    implementation(projects.core.common)

    implementation(projects.core.walletAdaptor)
    implementation(projects.core.web3)
  }
}
