package org.codecrafterslab.build

import org.codecrafterslab.gradle.maven.PublishLocalPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Usage
import org.gradle.api.internal.tasks.JvmConstants
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.jvm.tasks.Jar

class PublishPlugin : Plugin<Project> {

    companion object {
        const val MAVEN_PUBLICATION_NAME: String = "maven"
    }

    override fun apply(project: Project) {
        project.plugins.apply(MavenPublishPlugin::class.java)
        project.plugins.apply(PublishLocalPlugin::class.java)
        // java gradle plugin 与当前插件存在重复发布功能，否则发布时可能会出错
        project.afterEvaluate {
            if (!project.pluginManager.hasPlugin("java-gradle-plugin")) {
                with(project.extensions.getByType(PublishingExtension::class.java)) {
                    with(publications.create(MAVEN_PUBLICATION_NAME, MavenPublication::class.java)) {
                        softwareComponent(project, this)
                        configureVersionMapping(this)
                    }
                }
            }
        }
    }

    private fun softwareComponent(project: Project, publication: MavenPublication) {
        with(project) {
            plugins.withType(JavaPlugin::class.java).all {
                if ((tasks.getByName(JavaPlugin.JAR_TASK_NAME) as Jar).isEnabled) {
                    components.matching { JvmConstants.JAVA_MAIN_COMPONENT_NAME == it.name }
                        .all { publication.from(this) }
                }
            }
            plugins.withType(JavaPlatformPlugin::class.java).all {
                components.matching { "javaPlatform" == it.name }
                    .all { publication.from(this) }
            }
        }

    }

    private fun configureVersionMapping(publication: MavenPublication) {
        publication.versionMapping {
            usage(Usage.JAVA_API) { fromResolutionOf(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME) }
            usage(Usage.JAVA_RUNTIME) { fromResolutionResult() }
        }
    }
}