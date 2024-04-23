package org.codecrafterslab.gradle.maven

import groovy.util.logging.Slf4j
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import java.io.File

@Slf4j
class PublishLocalPlugin : Plugin<Project> {
    companion object {
        /**
         * Name of the `mavenRepository` configuration.
         */
        const val MAVEN_REPOSITORY_CONFIGURATION_NAME: String = "mavenRepository"

        /**
         * Name of the task that publishes to the project repository.
         */
        const val PUBLISH_TO_PROJECT_REPOSITORY_TASK_NAME = "publishMavenPublicationToProjectRepository"

        const val publish_path = ""
    }

    override fun apply(project: Project) {
        project.plugins.withType(MavenPublishPlugin::class.java) {
            /* 发布到项目根目录下 */
            val repositoryLocation = File(project.rootProject.layout.buildDirectory.get().asFile, "maven-repository")
            val publishing = project.extensions.getByType(PublishingExtension::class.java)

            val mavenArtifactRepositoryAction = MavenArtifactRepositoryAction("project", repositoryLocation)
            // todo 如何将将仓库添加到首位
//            project.repositories.maven(mavenArtifactRepositoryAction)

            project.gradle.settingsEvaluated {
                dependencyResolutionManagement {
                    repositories {
                        maven(mavenArtifactRepositoryAction)
                    }
                }
            }
            publishing.repositories.maven(mavenArtifactRepositoryAction)

            project.tasks.matching { it.name == PUBLISH_TO_PROJECT_REPOSITORY_TASK_NAME }.all {
                setUpProjectRepository(project, this, repositoryLocation)
            }
        }
    }

    private fun setUpProjectRepository(project: Project, publishTask: Task, repositoryLocation: File) {
        publishTask.doFirst(CleanAction(repositoryLocation))
        val projectRepository = project.configurations.create(MAVEN_REPOSITORY_CONFIGURATION_NAME)
        project.artifacts.add(projectRepository.name, repositoryLocation) {
            builtBy(publishTask)
        }
        val target = projectRepository.dependencies
        project.plugins.withType(JavaPlugin::class.java).all {
            addMavenRepositoryDependencies(project, JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, target)
        }
        project.plugins.withType(JavaLibraryPlugin::class.java).all {
            addMavenRepositoryDependencies(project, JavaPlugin.API_CONFIGURATION_NAME, target)
        }
        project.plugins.withType(JavaPlatformPlugin::class.java).all {
            addMavenRepositoryDependencies(project, JavaPlatformPlugin.API_CONFIGURATION_NAME, target)
        }
    }

    private fun addMavenRepositoryDependencies(
        project: Project,
        sourceConfigurationName: String,
        target: DependencySet,
    ) {
        project.configurations.getByName(sourceConfigurationName).dependencies.withType(ProjectDependency::class.java)
            .all {
                val dependencyDescriptor: MutableMap<String, String?> = HashMap()
                dependencyDescriptor["path"] = this.dependencyProject.path
                dependencyDescriptor["configuration"] = MAVEN_REPOSITORY_CONFIGURATION_NAME
                target.add(project.dependencies.project(dependencyDescriptor))
            }
    }

    private class CleanAction(private val root: File) : Action<Task> {
        override fun execute(task: Task) {
            val project = task.project
            val relativeTo = project.projectDir.relativeTo(project.rootProject.projectDir).path.split(File.separator)
                .joinToString("/")
            if (relativeTo == "") {
                project.delete(root)
            } else {
                project.delete(File(root, "${project.group}.${project.name}".replace(".", "/")))
            }
        }
    }

    private class MavenArtifactRepositoryAction(val name: String, private val location: File) :
        Action<MavenArtifactRepository> {
        override fun execute(repository: MavenArtifactRepository) {
            repository.name = name
            repository.url = location.toURI()
        }
    }


}