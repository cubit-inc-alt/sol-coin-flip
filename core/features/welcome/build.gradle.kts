import org.gradle.kotlin.dsl.implementation

plugins {
  alias(libs.plugins.multiplatform.library.compose)
  alias(libs.plugins.kotlinx.serilization)
}


kotlin{
  sourceSets.commonMain.dependencies {
    implementation(libs.solana)
    implementation(libs.solana.rpc)
    implementation(libs.solana.eddsa)

    implementation(projects.core.sol)
  }
}
