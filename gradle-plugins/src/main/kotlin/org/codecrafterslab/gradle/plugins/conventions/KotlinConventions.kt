package org.codecrafterslab.gradle.plugins.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
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
        val kotlinOptions = compile.kotlinOptions
        kotlinOptions.apiVersion = "1.9"
        kotlinOptions.languageVersion = "1.9"
        // kotlinOptions.jvmTarget = "1.8"
//        kotlinOptions.allWarningsAsErrors = true
        val freeCompilerArgs = ArrayList(compile.kotlinOptions.freeCompilerArgs)
        if (!freeCompilerArgs.contains("-Xsuppress-version-warnings")) {
            freeCompilerArgs.add("-Xsuppress-version-warnings")
            compile.kotlinOptions.freeCompilerArgs = freeCompilerArgs
        }

    }
}