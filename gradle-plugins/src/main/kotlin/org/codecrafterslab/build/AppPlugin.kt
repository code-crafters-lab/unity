package org.codecrafterslab.build

import org.codecrafterslab.gradle.plugins.ConventionsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

class AppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(ApplicationPlugin::class.java)
        project.plugins.apply(ConventionsPlugin::class.java)
    }
}