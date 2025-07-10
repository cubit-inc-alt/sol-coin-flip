import app.namespace
import app.resourcesNamespace


plugins {
  alias(libs.plugins.multiplatform.library.compose)
}

compose.resources {
    publicResClass = true
    packageOfResClass = resourcesNamespace
    generateResClass = always
}
