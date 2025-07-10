import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import app.apply
import app.configureAndroidCompose
import app.configureKotlinAndroid
import app.configureKotlinCompilation
import app.configureTestLogging
import app.libs

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.plugins.androidApplication)
            apply(libs.plugins.kotlinAndroid)
            apply(libs.plugins.module)
        }

        configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            configureAndroidCompose(this)
            configureKotlinCompilation()
        }

        dependencies {
            add("testImplementation", libs.junit.get())
        }

        configureTestLogging()
    }
}
