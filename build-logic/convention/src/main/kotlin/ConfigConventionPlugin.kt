import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import com.codingfeline.buildkonfig.gradle.TargetConfigDsl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import app.VariantDimension.dev
import app.VariantDimension.local
import app.VariantDimension.prod
import app.apply
import app.getAndroidBuildVariantOrNull
import app.libs
import app.loadProperties
import app.namespace

class ConfigConventionPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(libs.plugins.multiplatform.library.asProvider())
      apply(libs.plugins.buildkonfig)
    }

    project.extra.set("buildkonfig.flavor", currentBuildVariant())

    extensions.configure<BuildKonfigExtension> {
      this.exposeObjectWithName
      objectName = "CoinFlipConfig"
      exposeObjectWithName = "CoinFlipConfig"
      packageName = target.namespace

      defaultConfigs(local) {
        field("environment", local)
        appConfig("development")
      }

      defaultConfigs(dev) {
        field("environment", dev)
        appConfig("development")
      }

      defaultConfigs(prod) {
        field("environment", prod)
        appConfig("production")
      }

      defaultConfigs {
        field("environment", prod)
        appConfig("production")
      }
    }
  }
}

context(Project)
private fun TargetConfigDsl.appConfig(file: String) {
  val properties = loadProperties("config/app/$file.config")

  properties.stringPropertyNames()
    .forEach { key ->
      field(key.asConfigKey(), properties.getProperty(key))
    }
}

private fun String.asConfigKey() = this.split(".", "-")
  .mapIndexed { index: Int, s: String -> if (index == 0) s else s.uppercaseFirstChar() }
  .joinToString("")

private fun <T> TargetConfigDsl.field(key: String, value: T) {
  val spec = when (value) {
    is String -> FieldSpec.Type.STRING
    is Int -> FieldSpec.Type.INT
    is Float -> FieldSpec.Type.FLOAT
    is Long -> FieldSpec.Type.LONG
    is Boolean -> FieldSpec.Type.BOOLEAN
    else -> error("Unsupported build config value '$value' for '$key'")
  }

  buildConfigField(spec, key, value.toString().trim().removeSurrounding("\""))
}

private fun Project.currentBuildVariant(): String {
  val variants = setOf(local, dev, prod)

  return getAndroidBuildVariantOrNull()
    ?: System.getenv()["ENVIRONMENT"]
      .toString()
      .takeIf { it in variants } ?: prod
}
