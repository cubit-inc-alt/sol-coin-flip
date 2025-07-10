package app

import org.gradle.api.Project
import java.util.Properties
import java.util.regex.Pattern

val Project.namespace: String
  get() = projectPathAsPackage()

val Project.resourcesNamespace: String
  get() = "$namespace.generated.resources"

private fun Project.projectPathAsPackage() = path
  .split(":")
  .filter { it.isNotBlank() }
  .joinToString(".")

@Suppress("ConstPropertyName")
object VariantDimension {
  const val name = "variant"
  const val local = "local"
  const val dev = "development"
  const val prod = "production"
}

fun Project.getAndroidBuildVariantOrNull(): String? {
  val taskRequestsStr = gradle.startParameter.taskRequests.toString()
  val pattern: Pattern = if (taskRequestsStr.contains("assemble")) {
    Pattern.compile("assemble(\\w+)(Release|Debug)")
  } else {
    Pattern.compile("bundle(\\w+)(Release|Debug)")
  }

  val matcher = pattern.matcher(taskRequestsStr)
  return if (matcher.find()) {
    matcher.group(1).lowercase()
  } else {
    null
  }
}

fun Project.loadProperties(name: String) = Properties().also { properties ->
  with(rootProject.file("$name.properties")) {
    if (exists())
      properties.load(reader())
  }
}
