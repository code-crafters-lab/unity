package org.codecrafterslab.build

import org.codecrafterslab.gradle.plugins.ConventionsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin

/**
 * 组件库插件
 * @since 0.4.0
 */
class LibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(JavaLibraryPlugin::class.java)
        project.plugins.apply(ConventionsPlugin::class.java)
    }
}