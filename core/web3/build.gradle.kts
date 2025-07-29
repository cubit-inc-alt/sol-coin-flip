plugins {
  alias(libs.plugins.multiplatform.library.compose)
  alias(libs.plugins.config)
  alias(libs.plugins.kotlinx.serilization)
}
kotlin {
  sourceSets.commonMain.dependencies {
    implementation(projects.core.resources)

    implementation(libs.solana)
    implementation(libs.solana.rpc)
    implementation(libs.solana.base58)

    implementation(libs.buffer)
    implementation("io.github.funkatronics:multimult:0.2.3")
  }

}
