package app

import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

internal fun KotlinMultiplatformExtension.configureMultiplatformTargets() {
  targets.all {
    compilations.all {
      compileTaskProvider.configure {
        configureKotlinCompilation()
      }
    }
  }

  applyDefaultHierarchyTemplate()

  androidTarget()

  iosArm64()
  iosSimulatorArm64()
}

fun KotlinCompilationTask<*>.configureKotlinCompilation() {
  compilerOptions {
    allWarningsAsErrors.set(false)
    freeCompilerArgs.addAll(
      listOf(
        "-Xcontext-receivers",
        "-Xexpect-actual-classes"
      ),
    )
    optIn.addAll(
      listOf(
        "kotlin.experimental.ExperimentalObjCRefinement",
        "kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi",
      ),
    )
  }
}

fun Project.configureTestLogging() {
  tasks.withType<AbstractTestTask>().configureEach {
    testLogging {
      events.addAll(
        listOf(
          TestLogEvent.FAILED,
          TestLogEvent.PASSED,
          TestLogEvent.SKIPPED,
          TestLogEvent.STANDARD_ERROR,
          TestLogEvent.STANDARD_OUT,
        ),
      )
      exceptionFormat = TestExceptionFormat.FULL
    }
  }
}
