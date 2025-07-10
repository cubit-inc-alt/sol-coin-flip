import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.ktlint)
  alias(libs.plugins.detekt)
  `kotlin-dsl`
}

dependencies {
  compileOnly(libs.plugins.kotlinMultiplatform.toDep())
  compileOnly(libs.plugins.jetbrainsCompose.toDep())
  compileOnly(libs.plugins.kotlinAndroid.toDep())
  compileOnly(libs.plugins.androidApplication.toDep())
  compileOnly(libs.plugins.androidLibrary.toDep())
  compileOnly(libs.plugins.ktlint.toDep())
  compileOnly(libs.plugins.detekt.toDep())
  compileOnly(libs.plugins.buildkonfig.toDep())
  compileOnly(libs.ksp.gradlePlugin)
  compileOnly(libs.buildkonfigCompiler)
  compileOnly(libs.room.gradlePlugin)
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

ktlint {
  version = rootProject.libs.versions.ktlint.get()

  filter {
    exclude {
      it.file.absoluteFile.startsWith(layout.buildDirectory.asFile.get().absolutePath)
    }
  }
}

detekt {
  config.setFrom(rootProject.files("../config/detekt/detekt.yml"))
}
repositories {
  mavenCentral()
  gradlePluginPortal()
  google()
}

gradlePlugin {
  plugins {
    register("modulePlugin") {
      id = libs.plugins.module.get().pluginId
      implementationClass = "ModuleConventionPlugin"
    }

    register("multiplatformLibraryPlugin") {
      id = libs.plugins.multiplatform.library.asProvider().get().pluginId
      implementationClass = "MultiplatformLibraryConventionPlugin"
    }

    register("multiplatformComposeLibraryPlugin") {
      id = libs.plugins.multiplatform.library.compose.get().pluginId
      implementationClass = "MultiplatformComposeLibraryConventionPlugin"
    }

    register("androidApplication") {
      id = libs.plugins.android.app.get().pluginId
      implementationClass = "AndroidApplicationConventionPlugin"
    }

    register("config") {
      id = libs.plugins.config.get().pluginId
      implementationClass = "ConfigConventionPlugin"
    }

    register("androidRoom") {
      id = libs.plugins.room.get().pluginId
      implementationClass = "AndroidRoomConventionPlugin"
    }
    register("feature") {
      id = libs.plugins.feature.get().pluginId
      implementationClass = "FeatureConventionPlugin"
    }
  }
}


tasks.withType<KotlinCompile> {
  compilerOptions {
    freeCompilerArgs.addAll(
      "-Xcontext-receivers",
    )
  }
}

fun Provider<PluginDependency>.toDep() = map {
  "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
