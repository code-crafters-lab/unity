package org.codecrafterslab.publish

import org.codecrafterslab.gradle.utils.PropertyUtils.Companion.getPriorityProperty
import org.gradle.api.Project
import java.net.URI

class Nexus3Plugin : MavenBasePlugin() {
    override fun apply(project: Project) {
        project.plugins.apply(PublishPlugin::class.java)
        super.apply(project)
    }

    override fun getMavenName(): String {
        return "nexus"
    }

    override fun getMavenUrl(project: Project): URI {
        val nexus3Host = getPriorityProperty("dev.opts.${getMavenName()}.host", project, "http://localhost:8081")
        return project.uri("${nexus3Host}/repository/maven-${getVersionType(project)}/")
    }

}