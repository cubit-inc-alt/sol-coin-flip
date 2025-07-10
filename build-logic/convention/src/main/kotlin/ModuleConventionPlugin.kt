import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getByType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import app.apply
import app.libs

class ModuleConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(target.pluginManager) {
            apply(libs.plugins.ktlint)
            apply(libs.plugins.detekt)
        }

        configureKtLint()
        configureDetekt()
    }
}

private fun Project.configureKtLint(): KtlintExtension =
    extensions.getByType<KtlintExtension>().apply {
        version = rootProject.libs.versions.ktlint.get()

        enableExperimentalRules = false
        coloredOutput = true

        filter {
            exclude {
                it.file.absoluteFile.startsWith(layout.buildDirectory.asFile.get().absolutePath)
            }
        }
    }

private fun Project.configureDetekt(): DetektExtension =
    extensions.getByType<DetektExtension>().apply {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    }
