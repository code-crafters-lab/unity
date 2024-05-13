package org.codecrafterslab.gradle.plugins.conventions

import groovy.namespace.QName
import groovy.util.Node
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.XmlProvider
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.attributes.Usage
import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.internal.tasks.JvmConstants
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.*
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar

@Suppress("unused")
class MavenPublishingConventions : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.withType(MavenPublishPlugin::class.java).all {
            with(project.extensions.getByType(PublishingExtension::class.java)) {
                /* 1. 解析需要发布到的仓库 */
                resolvePublishRepository(project, repositories)

                /* 2. 发布配置 */
                publications.withType(MavenPublication::class.java).matching {
                    // https://docs.gradle.org/current/userguide/java_gradle_plugin.html
                    // 排除 java-gradle-plugin 的发布配置
                    it.name != "pluginMaven" && !it.name.contains("PluginMarkerMaven")
                }.all {
                    customizeMavenPublication(this, project)
                }
            }
        }
    }

    private fun customizeMavenPublication(publication: MavenPublication, project: Project) {
        customizePom(publication.pom, project)
        project.plugins.withType(JavaPlugin::class.java).all {
            customizeJavaMavenPublication(publication, project)
        }
    }

    private fun customizeJavaMavenPublication(publication: MavenPublication, project: Project) {
        addMavenOptionalFeature(project, publication)
        configureSoftwareComponent(project, publication)
        configureVersionMapping(publication)
    }

    private fun configureSoftwareComponent(project: Project, publication: MavenPublication) {
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

    /**
     * Add a feature that allows maven plugins to declare optional dependencies that
     * appear in the POM.
     * @param project the project to add the feature to
     */
    private fun addMavenOptionalFeature(project: Project, publication: MavenPublication) {
        val extension = project.extensions.getByType(JavaPluginExtension::class.java)
        extension.registerFeature("mavenOptional") {
            usingSourceSet(extension.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME))
        }
        val javaComponent =
            project.components.findByName(JvmConstants.JAVA_MAIN_COMPONENT_NAME) as AdhocComponentWithVariants
        javaComponent.addVariantsFromConfiguration(project.configurations.findByName("mavenOptionalRuntimeElements")!!) {
            mapToOptional()
        }
        suppressMavenOptionalFeatureWarnings(publication)
    }

    private fun suppressMavenOptionalFeatureWarnings(publication: MavenPublication) {
        publication.suppressPomMetadataWarningsFor("mavenOptionalApiElements")
        publication.suppressPomMetadataWarningsFor("mavenOptionalRuntimeElements")
    }

    private fun customizePom(pom: MavenPom, project: Project) {
        with(pom) {
            name.set(project.provider { project.name })
            description.set(project.provider { project.description })
            customizePackaging(pom, project)
            licenses { customizeLicences(this) }
            developers { customizeDevelopers(this) }
            scm { customizeScm(this, project) }
            withXml { customizeXml(this, project) }

        }
    }

    private fun customizeXml(xmlProvider: XmlProvider, project: Project) {
        val root = xmlProvider.asNode()

        // 移除 platform 插件自动生成的 dependencyManagement
        val dependencyManagement = findDependencyManagement(root)
        if (dependencyManagement != null) {
            root.remove(dependencyManagement)
        }
    }

    private fun findDependencyManagement(parent: Node): Node? {
        for (child in parent.children()) {
            if (child is Node) {
                if ((child.name() is QName) && "dependencyManagement" == (child.name() as QName).localPart) {
                    return child
                }
                if ("dependencyManagement" == child.name()) {
                    return child
                }
            }
        }
        return null
    }

    private fun customizeScm(mavenPomScm: MavenPomScm, project: Project) {

    }

    private fun customizeDevelopers(developers: MavenPomDeveloperSpec) {
        developers.developer {
            id.set("coffee377")
            name.set("Wu Yujie")
            email.set("coffee377@dingtalk.com")
        }
    }

    private fun customizeLicences(mavenPomLicense: MavenPomLicenseSpec) {

    }

    private fun customizePackaging(pom: MavenPom, project: Project) {
    }


    private fun resolvePublishRepository(project: Project, repositories: RepositoryHandler) {
    }


}