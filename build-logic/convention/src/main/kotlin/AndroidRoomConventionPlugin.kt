import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import app.libs
import app.apply


class AndroidRoomConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {


    with(target) {

      with(pluginManager) {
        apply(libs.plugins.android.room)
        apply(libs.plugins.ksp)
      }

      extensions.configure<KspExtension> {
        arg("room.generateKotlin", "true")
      }

      extensions.configure<RoomExtension> {
        // The schemas directory contains a schema file for each version of the Room database.
        // This is required to enable Room auto migrations.
        // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
        schemaDirectory("$projectDir/schemas")
      }

      configure<KotlinMultiplatformExtension> {
        sourceSets.commonMain.dependencies {
          api(libs.room.runtime.get())
          implementation(libs.sqlite.bundled.get())

        }
      }

      val libs = target.libs
      target.dependencies {
        add("kspAndroid", libs.room.compiler)
        add("kspIosSimulatorArm64", libs.room.compiler)
        add("kspIosArm64", libs.room.compiler)
      }
    }
  }
}
