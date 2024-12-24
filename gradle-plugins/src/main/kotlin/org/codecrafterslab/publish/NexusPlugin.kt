package org.codecrafterslab.publish

import org.codecrafterslab.VersionType
import org.codecrafterslab.gradle.utils.PropertyUtils.Companion.getPriorityProperty
import org.gradle.api.Project

open class NexusPlugin : MavenBasePlugin() {
    override fun apply(project: Project) {
        project.plugins.apply(PublishPlugin::class.java)
        super.apply(project)
    }

    override fun getMavenName(): String = "nexus"

    override fun getRepositoryId(versionType: VersionType, project: Project): String {
        val prefix = getPriorityProperty("dev.opts.${getMavenName()}.repository.id.prefix", project, "").toString()
        return "${prefix}${super.getRepositoryId(versionType, project)}"
    }
}