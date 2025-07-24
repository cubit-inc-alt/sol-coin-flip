plugins {
  alias(libs.plugins.multiplatform.library.compose)
  alias(libs.plugins.kotlinx.serilization)
}


kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.datastore)
      implementation(projects.core.database)
      implementation(projects.core.common)
      implementation(projects.core.network)
      implementation(projects.core.resources)

      api(libs.result)
      api(projects.core.models)

      implementation(libs.solana)
      implementation(libs.solana.rpc)
      implementation(libs.solana.eddsa)
      implementation(libs.solana.base58)

      implementation("com.ionspin.kotlin:multiplatform-crypto-libsodium-bindings:0.9.2")


//      implementation(libs.multiplatform.crypto.libsodium.bindings)
    }
  }
}


