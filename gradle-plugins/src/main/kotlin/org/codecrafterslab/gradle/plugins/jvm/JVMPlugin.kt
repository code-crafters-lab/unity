package org.codecrafterslab.gradle.plugins.jvm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import java.util.*

class JVMPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.tasks) {
            val targets = listOf("8", "11")

            targets.forEach { target ->
                val version =
                    target.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                register("compileJava${version}", JavaCompile::class.java) {
                    group = "jvm"
                    val mainSourceSet =
                        project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                    source(mainSourceSet.java.srcDirs)
                    classpath = mainSourceSet.compileClasspath
                    destinationDirectory.set(project.layout.buildDirectory.dir("classes/java/${target}"))
                    options.isIncremental = true
                    options.release.set(target.toInt())
                }

                register("jre${target}Jar", Jar::class.java) {
                    group = "jvm"
                    project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName("")
                    dependsOn("compileJava${version}", getByName("processResources"))
                    // 设置Jar文件的基本名称
                    archiveBaseName.set(project.name)
                    archiveClassifier.set("jre${target}")
                    from(
                        project.layout.buildDirectory.dir("classes/java/${target}"),
                        project.layout.buildDirectory.dir("resources/main")
                    )
                }
            }

            getByName("assemble").dependsOn(targets.map { "jre${it}Jar" })

        }
    }
}
