package org.codecrafterslab.publish

import org.gradle.api.Project

class Nexus3Plugin : MavenBasePlugin() {
    override fun apply(project: Project) {
        project.plugins.apply(PublishPlugin::class.java)
        super.apply(project)
    }

    override fun getMavenName(): String = "nexus"

}