package org.codecrafterslab.gradle.plugins.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinConventions : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withId("org.jetbrains.kotlin.jvm") {
            project.tasks.withType(KotlinCompile::class.java) {
                configure(this)
            }
        }
    }

    private fun configure(compile: KotlinCompile) {
        compile.compilerOptions {
            languageVersion.set(KotlinVersion.KOTLIN_1_9)
            apiVersion.set(KotlinVersion.KOTLIN_1_9)
            suppressWarnings.set(true)
            if (!freeCompilerArgs.get().contains("-Xsuppress-version-warnings")) {
                freeCompilerArgs.add("-Xsuppress-version-warnings")
            }
            progressiveMode.set(true)
            verbose.set(true)
        }
    }
}