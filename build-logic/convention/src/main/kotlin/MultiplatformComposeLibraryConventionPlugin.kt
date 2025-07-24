import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import app.apply
import app.configureAndroidCompose
import app.configureKotlinAndroid
import app.libs


class MultiplatformComposeLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(libs.plugins.multiplatform.library.asProvider())
      apply(libs.plugins.jetbrainsCompose)
      apply(libs.plugins.compose.compiler)
    }

    configure<LibraryExtension> {
      configureKotlinAndroid(this)
      configureAndroidCompose(this)
    }

    configure<KotlinMultiplatformExtension> {
      val extension = (extensions.getByName("compose") as ComposePlugin.Dependencies)

      sourceSets.commonMain.dependencies {
        implementation(extension.material3)
        implementation(extension.ui)
        implementation(extension.foundation)
        implementation(extension.runtime)
        implementation(extension.materialIconsExtended)
        implementation(extension.components.resources)
        implementation(extension.components.uiToolingPreview)

        implementation(libs.jetbrain.navigation.compose)
        implementation(libs.jetbrain.lifecycle.viewmodel)
        implementation(libs.jetbrain.lifecycle.viewmodel.compose)
      }
    }
  }
}
