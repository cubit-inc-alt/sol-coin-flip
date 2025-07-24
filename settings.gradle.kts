enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

rootProject.name = "CoinFlip"

include(":android")
include(":core")
include(":core:analytics")
include(":core:app")
include(":core:common")
include(":core:resources")
include(":core:designSystem")
include(":core:ui")
include(":core:datastore")
include(":core:network")
include(":core:models")
include(":core:database")
include(":core:data")
include(":core:sol")
include(":core:features:welcome")
include(":core:features:connect-wallet")
include(":core:components:coil")
