package org.codecrafterslab.build

import org.codecrafterslab.gradle.plugins.conventions.MavenPublishingConventions
import org.codecrafterslab.gradle.plugins.maven.PublishLocalPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin


class PublishPlugin : Plugin<Project> {

    companion object {
        const val MAVEN_PUBLICATION_NAME: String = "maven"
    }

    override fun apply(project: Project) {
        project.plugins.apply(MavenPublishPlugin::class.java)
        project.plugins.apply(PublishLocalPlugin::class.java)
        project.plugins.apply(MavenPublishingConventions::class.java)
        // java gradle plugin 与当前插件存在重复发布功能，否则发布时可能会出错
        project.afterEvaluate {
            if (!project.pluginManager.hasPlugin("java-gradle-plugin")) {
                with(project.extensions.getByType(PublishingExtension::class.java)) {
                    publications.create(MAVEN_PUBLICATION_NAME, MavenPublication::class.java)
                }
            }
        }
    }

}