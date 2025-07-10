package app

import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

fun PluginManager.apply(provider: Provider<PluginDependency>) {
    apply(provider.get().pluginId)
}
