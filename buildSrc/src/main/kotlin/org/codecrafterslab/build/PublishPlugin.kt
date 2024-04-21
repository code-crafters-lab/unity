package org.codecrafterslab.build

import org.codecrafterslab.gradle.maven.PublishLocalPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.jvm.tasks.Jar

class PublishPlugin : Plugin<Project> {

    companion object {
        const val MAVEN_PUBLICATION_NAME: kotlin.String = "maven"
    }

    override fun apply(project: Project) {
        project.plugins.apply(MavenPublishPlugin::class.java)
        project.plugins.apply(PublishLocalPlugin::class.java)
        // java gradle plugin 与当前插件存在重复发布功能，否则发布时可能会出错
        project.afterEvaluate {
            if (!project.pluginManager.hasPlugin("java-gradle-plugin")) {
                val publishing = project.extensions.getByType(PublishingExtension::class.java)
                val mavenPublication =
                    publishing.publications.create(MAVEN_PUBLICATION_NAME, MavenPublication::class.java)

                project.plugins.withType(JavaPlugin::class.java) {
                    if ((project.tasks.getByName(JavaPlugin.JAR_TASK_NAME) as Jar).isEnabled) {
                        project.components.matching { component -> component.name == "java" }
                            .all { mavenPublication.from(this) }
                    }
                }

                project.plugins.withType(JavaPlatformPlugin::class.java) {
                    project.components.matching { component -> component.name == "javaPlatform" }
                        .all { mavenPublication.from(this) }
                }
            }
        }
    }
}