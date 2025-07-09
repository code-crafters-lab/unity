package org.codecrafterslab.gradle.plugins.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalDependency
import org.gradle.api.plugins.JavaPlugin

class DependencyConventions : Plugin<Project> {

    override fun apply(project: Project) {
        val springBootVersion = project.properties.getOrDefault("spring-boot.version", "2.5.15").toString()
        val lombokVersion = project.properties.getOrDefault("lombok.version", "1.18.38").toString()

        project.configurations.all {
            resolutionStrategy {
                eachDependency {
                    if (requested.group == "org.springframework.boot") {
                        useVersion(springBootVersion)
                    }
                }
            }
        }

        project.configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME) {
            withDependencies {
                dependencies.forEach {
                    if (it.version == null && it is ExternalDependency) {
                        if ("${it.group}:${it.name}" == "org.projectlombok:lombok") {
                            it.version { require(lombokVersion) }
                        }
                        if ("${it.group}:${it.name}" == "org.springframework.boot:spring-boot-configuration-processor") {
                            it.version { require(springBootVersion) }
                        }
                    }
                }
            }
        }
    }
}
