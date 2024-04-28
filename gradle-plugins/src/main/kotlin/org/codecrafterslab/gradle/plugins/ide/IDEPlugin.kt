package org.codecrafterslab.gradle.plugins.ide

import org.codecrafterslab.gradle.utils.PropertyUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.ide.visualstudio.plugins.VisualStudioPlugin
import org.gradle.ide.xcode.plugins.XcodePlugin
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.idea.IdeaPlugin

/**
 * 集成开发环境插件配置
 */
class IDEPlugin : Plugin<Project> {
    companion object {
        const val IDE_PROPERTY_NAME: String = "dev.ide"
    }

    override fun apply(project: Project) {
        val ide = IDE.of(PropertyUtils.getPriorityProperty(IDE_PROPERTY_NAME, project, ""))
        when (ide) {
            IDE.ECLIPSE -> project.plugins.apply(EclipsePlugin::class.java)
            IDE.VISUAL_STUDIO -> project.plugins.apply(VisualStudioPlugin::class.java)
            IDE.XCODE -> project.plugins.apply(XcodePlugin::class.java)
            IDE.IDEA -> project.plugins.apply(IdeaPlugin::class.java)
            IDE.NONE -> {}
        }
    }
}