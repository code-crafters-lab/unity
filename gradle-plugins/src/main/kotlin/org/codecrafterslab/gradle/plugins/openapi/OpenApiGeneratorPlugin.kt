package org.codecrafterslab.gradle.plugins.openapi

import org.codecrafterslab.gradle.plugins.openapi.tasks.GenerateTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class OpenApiGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            tasks.apply {
                register("generateOpenApi", GenerateTask::class.java) {

                }
            }
        }
    }
}
