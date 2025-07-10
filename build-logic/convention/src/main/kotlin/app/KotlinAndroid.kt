package app

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.tasks.factory.AndroidUnitTest
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(extension: CommonExtension<*, *, *, *, *, *> ) =
    with(extension) {
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        defaultConfig {
            minSdk = libs.versions.android.minSdk.get().toString().toInt()

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        lint {
            warningsAsErrors = true
            abortOnError = true
            disable += listOf(
                "GradleDependency",
                "AndroidGradlePluginVersion",
            )
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        buildFeatures {
            buildConfig = true
        }

        tasks.withType<AndroidUnitTest> {
            useJUnitPlatform()
        }

        configureKotlinCompilation()

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

internal fun Project.configureAndroidCompose(extension: CommonExtension<*, *, *, *, *, *>) =
    with(extension) {
        buildFeatures {
            compose = true
        }
    }

/**
 * Configure base Kotlin options
 */
fun Project.configureKotlinCompilation() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions{
            jvmTarget.set(JvmTarget.JVM_21)
        }
        configureKotlinCompilation()
    }
}
