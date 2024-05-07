package org.codecrafterslab.build.settings

import org.codecrafterslab.build.settings.conventions.RepositoriesConventions
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

@Deprecated("use org.codecrafterslab.build.settings.ConventionsPlugin instead")
class RepositoriesPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.plugins.apply(RepositoriesConventions::class.java)
    }
}