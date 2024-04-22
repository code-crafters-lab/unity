package org.codecrafterslab.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

/**
 * 可选依赖插件
 * @since 0.4.0
 */
class OptionalDependencyPlugin : Plugin<Project> {
    companion object {
        /**
         * Name of the `optional` configuration.
         */
        const val OPTIONAL_CONFIGURATION_NAME: String = "optional"
    }


    override fun apply(project: Project) {
        val optional = project.configurations.create(OPTIONAL_CONFIGURATION_NAME)
        optional.isCanBeConsumed = false
        optional.isCanBeResolved = false

        project.plugins.withType(JavaPlugin::class.java) {
            project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.all {
                project.configurations.filter { it.name == compileClasspathConfigurationName || it.name == runtimeClasspathConfigurationName }
                    .forEach { it.extendsFrom(optional) }
//                project.configurations.getByName(compileClasspathConfigurationName).extendsFrom(optional)
//                project.configurations.getByName(runtimeClasspathConfigurationName).extendsFrom(optional)
            }
        }
    }
}