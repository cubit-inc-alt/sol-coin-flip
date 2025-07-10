import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import app.apply
import app.libs

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.multiplatform.library.compose)
            }

            configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    implementation(project(":core:ui"))
                    implementation(project(":core:designSystem"))
                    implementation(project(":core:common"))
                    implementation(project(":core:resources"))
                    implementation(project(":core:data"))
                    implementation(libs.kotlinx.serialization.json)
                    implementation(libs.result.get())
                }
            }
        }
    }
}
