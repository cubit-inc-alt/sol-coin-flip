plugins {
  alias(libs.plugins.multiplatform.library.compose)
  alias(libs.plugins.config)
  alias(libs.plugins.kotlinx.serilization)
}
kotlin {
  sourceSets.androidMain.dependencies {
    api(libs.bufferJvm)
  }

  sourceSets.commonMain.dependencies {
    implementation(projects.core.resources)

    api(libs.solana)
    api(libs.solana.rpc)
    api(libs.solana.base58)

    implementation("io.github.funkatronics:kborsh:0.1.1")

    api(libs.buffer)
  }

}
