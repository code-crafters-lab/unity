package org.codecrafterslab.gradle.plugins

import org.codecrafterslab.gradle.plugins.conventions.GrpcConventions
import org.gradle.api.Plugin
import org.gradle.api.Project

@Deprecated("Use org.codecrafterslab.gradle.plugins.conventions.GrpcConventions instead.")
class GRPCPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply(GrpcConventions::class.java)
    }
}