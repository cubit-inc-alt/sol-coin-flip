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

      api(libs.solana.base58)
      api("com.ionspin.kotlin:bignum:0.3.10")
    }
  }
}


