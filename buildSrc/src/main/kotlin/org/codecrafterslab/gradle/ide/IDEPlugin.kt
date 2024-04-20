package org.codecrafterslab.gradle.ide

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.ide.visualstudio.plugins.VisualStudioPlugin
import org.gradle.ide.xcode.plugins.XcodePlugin
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin
import java.io.File

/**
 * 集成开发环境插件配置
 */
class IDEPlugin : Plugin<Project> {
    companion object {
        const val IDE_PROPERTY_NAME: String = "ide"
    }

    override fun apply(project: Project) {
        val ide = IDE.of(project.rootProject.properties.getOrDefault(IDE_PROPERTY_NAME, "").toString())
        when (ide) {
            IDE.ECLIPSE -> project.plugins.apply(EclipsePlugin::class.java)
            IDE.VISUAL_STUDIO -> project.plugins.apply(VisualStudioPlugin::class.java)
            IDE.XCODE -> project.plugins.apply(XcodePlugin::class.java)
            IDE.IDEA -> project.plugins.apply(IdeaPlugin::class.java)
            IDE.NONE -> {}
        }
    }

    private fun isRootProject(project: Project): Boolean {
        return project.projectDir.relativeTo(project.rootProject.projectDir).path.split(File.separator)
            .joinToString("/") == ""
    }
}