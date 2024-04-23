package org.codecrafterslab.build.convention

import org.codecrafterslab.gradle.utils.FileOperationUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.file.FileOperations
import java.io.File

class RepositoriesPlugin : Plugin<Settings> {
    private lateinit var fileOperations: FileOperations

    override fun apply(settings: Settings) {
        fileOperations = FileOperationUtils.fileOperationsFor(settings)
        with(settings) {
            pluginManagement {
                repositories {
                    common(rootDir.parentFile)
                    maven { url = uri("https://maven.aliyun.com/repository/public") }
                }
            }
            dependencyResolutionManagement {
                repositories {
                    common(rootDir.parentFile)
                    maven {
                        url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public/"); isAllowInsecureProtocol =
                        true
                    }
                    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
                    maven { url = uri("https://repo.spring.io/plugins-release") }
                }
                // https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/resolve/RepositoriesMode.html
                // repositoriesMode = RepositoriesMode.PREFER_SETTINGS
            }
        }

    }

    private fun RepositoryHandler.common(rootDir: File) {
        maven {
            url = uri(File(rootDir, arrayOf(Project.DEFAULT_BUILD_DIR_NAME, "maven-repository").joinToString("/")))
        }
        mavenLocal()
        maven { url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public"); isAllowInsecureProtocol = true }
    }

    /**
     * 获取 URI
     */
    private fun uri(path: Any) = fileOperations.uri(path)
}