plugins {
  alias(libs.plugins.multiplatform.library.compose)
  alias(libs.plugins.kotlinx.serilization)
}

kotlin {

  sourceSets {

    commonMain.dependencies {
      implementation(projects.core.common)
      implementation(projects.core.datastore)
      implementation(projects.core.models)
      implementation(projects.core.analytics)
      implementation(projects.core.resources)

      with(libs.ktor) {
        with(client) {
          implementation(core)
          implementation(content.negotiation)
          implementation(logging)
          implementation(auth)
        }

        implementation(utils)
        implementation(client.websockets)
        implementation(serialization.kotlinx.json)
        implementation(io)
      }
      api(libs.result)
      implementation(libs.uuid)
    }

    androidMain.dependencies {
      implementation(libs.ktor.client.okhttp)
      implementation(libs.contextProvider)
    }

    iosMain.dependencies {
      implementation(libs.ktor.client.darwin)
    }
  }
}

dependencies {
  debugImplementation(libs.chucker)
  releaseImplementation(libs.chucker.noOp)
}
