package org.codecrafterslab.gradle

import org.codecrafterslab.ProjectInfo
import org.gradle.api.Project
import java.io.File


class ProjectInfoBase : ProjectInfo, Comparable<ProjectInfoBase> {
    private var project: Project? = null
    private var buildFile: File? = null
    private var dir: File

    private val path: String
    private val name: String

    constructor(buildFile: File) {
        this.buildFile = buildFile
        dir = buildFile.parentFile
        this.name = ""
        this.path = ""

//        dir = buildFile.parentFile
//        buildFileName = buildFile.name
//        val isDefaultName = buildFile.name == "build.gradle" || buildFile.name == "build.gradle.kts"
//        val isKotlin = buildFile.name.endsWith(".kts")
//        val paths = dir.relativeTo(rootFile).path.split(File.separator)
//        path = ":${paths.joinToString(":")}"
//
//        name = if (!isDefaultName) {
//            if (isKotlin) {
//                buildFile.name.replace(".gradle.kts", "")
//            } else {
//                buildFile.name.replace(".gradle", "")
//            }
//        } else {
//            "${rootFile.name}-${paths.joinToString("-")}"
//        }
    }

    constructor(project: Project) {
        this.project = project
        this.dir = project.projectDir
        this.name = project.name
        this.path = project.path
    }

    override fun isRootProject(): Boolean {
        return project!!.rootProject.path === ""
    }

    override fun getDir(): File {
        return dir
    }

    override fun getBuildFileName(): String {
        return buildFile!!.name
    }

    override fun getPath(): String {
        return path
    }

    override fun getName(): String {
        return name
    }

    override fun compareTo(other: ProjectInfoBase): Int {
        return path.compareTo(other.path)
    }
}