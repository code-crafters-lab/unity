package org.codecrafterslab.gradle.plugins.conventions

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.jvm.tasks.Jar
import org.gradle.testretry.TestRetryPlugin
import org.gradle.testretry.TestRetryTaskExtension

class JavaConventions : Plugin<Project> {

    companion object {
        const val SOURCE_AND_TARGET_COMPATIBILITY: String = "1.8"
    }

    override fun apply(project: Project) {
        project.plugins.withType(JavaBasePlugin::class.java) {
            configureJavaConventions(project)
            configureAnnotationConventions(project)
            configureJavadocConventions(project)
            configureTestConventions(project)
            configureJarManifestConventions(project)
            configureJarOtherConventions(project)
            configureDependencyManagement(project)
            configureToolchain(project)
            configureClean(project)
        }
    }

    private fun configureClean(project: Project) {
        /* 清理任务增加删除目录 */
        project.tasks.withType(Delete::class.java) {
            // todo includeBuild 时清理父项目，子项目未清理
            delete("out", "build")
        }
    }

    private fun configureAnnotationConventions(project: Project) {
        project.plugins.withType(JavaPlugin::class.java) {
            with(project.configurations) {
                val annotationProcessor = getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
                val testAnnotationProcessor = getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
                val compileOnly = getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME)

                testAnnotationProcessor.extendsFrom(annotationProcessor)
                compileOnly.extendsFrom(annotationProcessor)
                getByName(JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME).extendsFrom(compileOnly)
//                getByName(JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME).extendsFrom(testAnnotationProcessor)
            }
        }
    }

    private fun configureToolchain(project: Project) {
        // todo 工具链配置
    }

    private fun configureDependencyManagement(project: Project) {
        // todo 依赖管理
    }


    private fun configureJarManifestConventions(project: Project) {
        project.tasks.withType(Jar::class.java) {
            manifest {
                val attributes: MutableMap<String, Any> = HashMap()
                attributes["Automatic-Module-Name"] = project.name.replace("-", ".")
                attributes["Created-By"] = String.format(
                    "%s (%s)", System.getProperty("java.version"), System.getProperty("java.specification.vendor")
                )
                attributes["Implementation-Title"] = project.name
                attributes["Implementation-Version"] = project.version
                attributes(attributes)
            }
        }
    }

    private fun configureJarOtherConventions(project: Project) {
        project.tasks.withType(Jar::class.java) {
            /* 重复文件策略 */
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            includeEmptyDirs = false
            exclude("rebel.xml")
        }
    }

    private fun configureTestConventions(project: Project) {
        project.tasks.withType(Test::class.java) {
            useJUnitPlatform()
            maxHeapSize = "1024M"
            this.mustRunAfter(project.tasks.withType(Checkstyle::class.java))
        }
        project.plugins.withType(JavaPlugin::class.java) {
            with(project.dependencies) {
                val junitVersion = project.properties.getOrDefault("junit-bom.version", "5.10.2")
                add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, platform("org.junit:junit-bom:${junitVersion}"))
                add(
                    JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME, "org.junit.platform:junit-platform-launcher"
                )
                // add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.junit.jupiter:junit-jupiter")
            }

        }
        project.plugins.apply(TestRetryPlugin::class.java)
        project.tasks.withType(Test::class.java).forEach { test ->
            project.plugins.withType(TestRetryPlugin::class.java) {
                with(test.extensions.getByType(TestRetryTaskExtension::class.java)) {
                    failOnPassedAfterRetry.set(true)
                    maxRetries.set(if (isCi()) 3 else 0)
                }
            }
        }
    }

    private fun configureJavadocConventions(project: Project) {
        project.tasks.withType(Javadoc::class.java) {
            options.encoding("UTF-8")
            if (options is StandardJavadocDocletOptions) {
                val standardJavadocDocletOptions = options as StandardJavadocDocletOptions
                standardJavadocDocletOptions.charSet("UTF-8")
                standardJavadocDocletOptions.addStringOption("Xdoclint:none", "-quiet")
                standardJavadocDocletOptions.tags?.add("email")
                standardJavadocDocletOptions.tags?.add("time")
            }
        }
    }

    private fun configureJavaConventions(project: Project) {
        val hasToolchainVersion = project.hasProperty("toolchainVersion")
        if (!hasToolchainVersion) {
            // fix 这里代码会导致依传递依赖版本获取不到
//            val javaPluginExtension = project.extensions.getByType(JavaPluginExtension::class.java)
//            javaPluginExtension.sourceCompatibility = JavaVersion.toVersion(SOURCE_AND_TARGET_COMPATIBILITY)
        }

        project.tasks.withType(JavaCompile::class.java) {
            if (options.encoding == null) {
                options.encoding = "UTF-8"
            }
            options.release.set(8)
            val args = options.compilerArgs
            if (!args.contains("-parameters")) {
                args.add("-parameters")
            }
            if (buildingWithJava8(project)) {
                args.addAll(
                    mutableListOf(
                        "-Werror", "-Xlint:unchecked", "-Xlint:deprecation", "-Xlint:rawtypes", "-Xlint:varargs"
                    )
                )
            }
        }
    }

    private fun buildingWithJava8(project: Project): Boolean {
        return !project.hasProperty("toolchainVersion") && JavaVersion.current() == JavaVersion.VERSION_1_8
    }

    private fun isCi(): Boolean {
        return System.getenv("CI").toBoolean()
    }


}