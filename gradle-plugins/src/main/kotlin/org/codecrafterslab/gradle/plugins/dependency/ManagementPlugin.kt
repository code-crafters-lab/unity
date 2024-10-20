package org.codecrafterslab.gradle.plugins.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaTestFixturesPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * 依赖管理插件
 * @since 0.4.0
 */
class ManagementPlugin : Plugin<Project> {
    companion object {
        /**
         * Name of the `management` configuration.
         */
        const val MANAGEMENT_CONFIGURATION_NAME: String = "management"
    }


    override fun apply(project: Project) {
        val configurations = project.configurations
        val management = configurations.create(MANAGEMENT_CONFIGURATION_NAME)

        management.isVisible = false
        management.isCanBeConsumed = false
        management.isCanBeResolved = false

        project.plugins.withType(JavaPlugin::class.java) {
            configurations.getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management)
            configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management)
            configurations.getByName(JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management)
            configurations.getByName(JavaPlugin.TEST_RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management)
        }

        project.plugins.withType(JavaTestFixturesPlugin::class.java) {
            configurations.getByName("testFixturesCompileClasspath").extendsFrom(management)
            configurations.getByName("testFixturesRuntimeClasspath").extendsFrom(management)
        }

        project.plugins.withType(MavenPublishPlugin::class.java) {
            project.extensions.getByType(PublishingExtension::class.java).publications {
                withType(MavenPublication::class.java) {
                    versionMapping {
                        allVariants {
                            fromResolutionResult()
                        }
                    }
                }
            }
        }

    }
}