package org.codecrafterslab.gradle

import org.codecrafterslab.gradle.ide.IDEPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.jvm.tasks.Jar

class BasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        configureClean(project)
        configureJavaCompile(project)
        configureJar(project)
        configureJavadoc(project)
        configureAnnotationProcessor(project)
        project.plugins.apply(OptionalDependencyPlugin::class.java)
        project.plugins.apply(IDEPlugin::class.java)
    }

    /**
     * 清理任务增加删除目录
     *
     * @param project Project
     */
    private fun configureClean(project: Project) {
        /* 清理任务增加删除目录 */
        project.tasks.withType(Delete::class.java) {
            delete("out", "build")
        }
    }

    /**
     * 编译配置
     *
     * @param project Project
     */
    private fun configureJavaCompile(project: Project) {
        project.tasks.withType(JavaCompile::class.java) {
            if (options.encoding == null) {
                options.encoding = "UTF-8"
            }
            options.compilerArgs.add("-parameters")
            if (JavaVersion.current().isJava8Compatible) {
                options.release.set(8)
            } else if (JavaVersion.current().isJava11Compatible) {
                options.release.set(11)
            }
        }
    }

    /**
     * 配置打包策略
     *
     * @param project Project
     */
    private fun configureJar(project: Project) {
        project.tasks.withType(Jar::class.java) {
            /* 重复文件策略，排除 */
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            includeEmptyDirs = false
            exclude("rebel.xml")
            manifest {
                val attributes: MutableMap<String, String> = HashMap()
                attributes["Created-By"] = String.format(
                    "%s (%s)",
                    System.getProperty("java.version"),
                    System.getProperty("java.specification.vendor")
                )
                attributes["Implementation-Title"] = project.name
                attributes["Implementation-Version"] = project.version.toString()
                attributes["Automatic-Module-Name"] = project.name.replace("-", ".")
                attributes(attributes)
            }
        }
    }

    private fun configureJavadoc(project: Project) {
        project.tasks.withType(Javadoc::class.java) {
            options.encoding("UTF-8")
            if (options is StandardJavadocDocletOptions) {
                (options as StandardJavadocDocletOptions).charSet("UTF-8")
            }
        }
    }

    private fun configureAnnotationProcessor(project: Project) {
        val configurations = project.configurations
        val annotationConfiguration = if (!project.pluginManager.hasPlugin("java")) {
            /* 不存 java 插件时，自己手动创建一个，保证兼容使用 */
            project.configurations.create(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
            project.configurations.create(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
        } else {
            configurations.getByName(JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
        }

        val testAnnotationConfiguration =
            configurations.getByName(JavaPlugin.TEST_ANNOTATION_PROCESSOR_CONFIGURATION_NAME)
        testAnnotationConfiguration.extendsFrom(annotationConfiguration)

        val compileOnlyConfiguration = configurations.getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME)
        compileOnlyConfiguration.extendsFrom(annotationConfiguration)

        val testCompileOnlyConfiguration = configurations.getByName(JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME)
        testCompileOnlyConfiguration.extendsFrom(compileOnlyConfiguration, testAnnotationConfiguration)
    }

}