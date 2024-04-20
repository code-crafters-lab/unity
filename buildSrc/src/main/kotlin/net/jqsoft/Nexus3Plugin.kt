package net.jqsoft

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

class Nexus3Plugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(MavenPublishPlugin::class.java) {
            val publishing = project.extensions.getByType(PublishingExtension::class.java)
            publishing.repositories.maven {
                name = "JinQiSoftNexus3"
                val versionType = if (project.version.toString().endsWith("SNAPSHOT", true)) "snapshots" else "releases"
                url = project.uri("http://nexus.jqk8s.jqsoft.net/repository/maven-${versionType}/")
                isAllowInsecureProtocol = true
                val user =
                    project.properties.getOrDefault("dev.opts.nexus.username", System.getenv("DEV_OPTS_NEXUS_USERNAME"))
                        .toString()
                val pwd =
                    project.properties.getOrDefault("dev.opts.nexus.password", System.getenv("DEV_OPTS_NEXUS_PASSWORD"))
                        .toString()
                credentials {
                    if (user != "") username = user
                    if (pwd != "") password = pwd
                }
            }
        }
    }
}