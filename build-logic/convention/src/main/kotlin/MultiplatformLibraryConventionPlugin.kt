import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import app.apply
import app.configureKotlinAndroid
import app.configureMultiplatformTargets
import app.configureTestLogging
import app.libs
import app.namespace


class MultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.plugins.androidLibrary)
            apply(libs.plugins.kotlinMultiplatform)
            apply(libs.plugins.module)
        }

        configure<LibraryExtension> {
            configureKotlinAndroid(this)
            namespace = target.namespace
        }

        configure<KotlinMultiplatformExtension> {
            configureMultiplatformTargets()

            sourceSets.commonMain.dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.jetbrain.lifecycle.viewmodel)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
            }

            sourceSets.commonTest.dependencies {
                implementation(libs.kotlin.test)
            }

            sourceSets.androidMain.dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.appcompat)
            }

            sourceSets["androidUnitTest"].dependencies {
                implementation(libs.junit.get())
            }
        }

        configureTestLogging()
    }
}
