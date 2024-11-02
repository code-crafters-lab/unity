package org.codecrafterslab.build.settings.conventions

import org.codecrafterslab.gradle.ProjectInfoBase
import org.codecrafterslab.gradle.utils.FileOperationUtils
import org.gradle.api.Plugin
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.file.FileOperations

class AutoIncludeConventions : Plugin<Settings> {
    private lateinit var fileOperations: FileOperations
    override fun apply(settings: Settings) {
        fileOperations = FileOperationUtils.fileOperationsFor(settings)
        settings.gradle.settingsEvaluated {
            autoInclude(settings)
        }
    }

    private fun autoInclude(settings: Settings) {
//        val autoIncludeProjectExtension = settings.extensions.getByType(AutoIncludeProjectExtension::class.java)
//        if (autoIncludeProjectExtension.isEnable.not()) return

        val rootDir = settings.rootDir

        fileTree(rootDir) {
            include("**/*.gradle", "**/*.gradle.kts")
            exclude("build", "**/gradle", "settings.gradle", "buildSrc", "/build.gradle", ".*", "out")
            val excludes = settings.gradle.startParameter.projectProperties["excludeProjects"]?.split(",")
            excludes?.let { exclude(it) }
        }
            .files
            .asSequence()
            .filter { file -> !file.parentFile.relativeTo(rootDir).name.isNullOrEmpty() }
            .map { file -> ProjectInfoBase(settings, file) }.sorted()
//            .filter { projectInfo ->
//                val find = autoIncludeProjectExtension.excludeProject.map { it.toRegex() }
//                    .find {
//                        it.matches(input = projectInfo.name)
//                    }
//                find == null
//            }
            .toList().forEach { it.include() }

    }

    /**
     * 获取文件树
     */
    private fun fileTree(baseDir: Any, configuration: ConfigurableFileTree.() -> Unit): ConfigurableFileTree {
        return fileOperations.fileTree(baseDir).also(configuration)
    }

}