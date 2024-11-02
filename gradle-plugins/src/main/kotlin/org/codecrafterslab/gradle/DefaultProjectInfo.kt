package org.codecrafterslab.gradle

import org.codecrafterslab.ProjectInfo
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import java.io.File


class ProjectInfoBase(private var settings: Settings, buildFile: File) : ProjectInfo, Comparable<ProjectInfoBase> {
    private var buildFile: File? = buildFile
    private var project: Project? = null
    private var dir: File
    private val path: String
    private val name: String

    init {
        val rootFile = settings.rootDir
        this.dir = buildFile.parentFile
        val isDefaultName = buildFile.name == "build.gradle" || buildFile.name == "build.gradle.kts"
        val isKotlin = buildFile.name.endsWith(".kts")
        val paths = dir.relativeTo(rootFile).path.split(File.separator)
        this.name = if (!isDefaultName) {
            if (isKotlin) {
                buildFile.name.replace(".gradle.kts", "")
            } else {
                buildFile.name.replace(".gradle", "")
            }
        } else {
            "${rootFile.name}-${paths.joinToString("-")}"
        }
        this.path = ":${paths.joinToString(":")}"
    }

    override fun isRootProject(): Boolean {
        return project!!.rootProject.path === ""
    }

    override fun getBuildFileName(): String {
        return buildFile!!.name
    }

    override fun getDir(): File {
        return dir
    }

    override fun getName(): String {
        return name
    }

    override fun getPath(): String {
        return path
    }

    override fun compareTo(other: ProjectInfoBase): Int {
        return path.compareTo(other.path)
    }

    fun include() {
        this.settings.include(this.path)
        processProjectInfo(this.settings, this)
    }

    private fun processProjectInfo(settings: Settings, it: ProjectInfo) {
        val projectDescriptor = settings.findProject(it.dir)
        projectDescriptor?.name = it.name
        projectDescriptor?.projectDir = it.dir
        projectDescriptor?.buildFileName = it.buildFileName
    }
}