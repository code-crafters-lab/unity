package org.codecrafterslab.build.settings

import org.codecrafterslab.build.settings.conventions.RepositoriesConventions
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class ConventionsPlugin : Plugin<Settings> {
    override fun apply(target: Settings) {
//        target.plugins.apply(AutoIncludeConventions::class.java)
        target.plugins.apply(RepositoriesConventions::class.java)
//        target.plugins.apply(InnerConventions::class.java)
    }
}