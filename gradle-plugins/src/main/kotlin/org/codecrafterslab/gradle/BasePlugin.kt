package org.codecrafterslab.gradle

import org.codecrafterslab.gradle.plugins.ConventionsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

@Deprecated("Use org.codecrafterslab.gradle.plugins.ConventionsPlugin instead.")
class BasePlugin : Plugin<Project> {
    
    override fun apply(project: Project) {
        project.plugins.apply(ConventionsPlugin::class.java)
    }

}