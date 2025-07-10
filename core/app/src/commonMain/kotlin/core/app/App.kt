package core.app

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import core.common.isDebugBuild

fun onAppStarted() {
    if (isDebugBuild()) {
        Napier.base(DebugAntilog())
    }
}
