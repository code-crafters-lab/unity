package org.codecrafterslab.gradle.plugins

import org.codecrafterslab.gradle.plugins.conventions.*
import org.codecrafterslab.gradle.plugins.dependency.OptionalPlugin
import org.codecrafterslab.gradle.plugins.ide.IDEPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class ConventionsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.plugins) {
            apply(JavaConventions::class.java)
            apply(KotlinConventions::class.java)
            apply(MavenPublishingConventions::class.java)
            apply(AsciidoctorConventions::class.java)
            apply(GrpcConventions::class.java)
            
            apply(OptionalPlugin::class.java)
            apply(IDEPlugin::class.java)
        }
    }
}