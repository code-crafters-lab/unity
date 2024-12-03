package org.codecrafterslab.publish

import org.codecrafterslab.VersionType
import org.codecrafterslab.gradle.utils.PropertyUtils.Companion.getPriorityProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.springframework.util.StringUtils
import java.net.URI

/**
 * 阿里云效仓库插件
 */
abstract class MavenBasePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.withType(MavenPublishPlugin::class.java) {
            val publishing = project.extensions.getByType(PublishingExtension::class.java)
            publishing.repositories.maven {
                name = getMavenName()
                url = getMavenUrl(project)
                val user = getPriorityProperty("dev.opts.${name.lowercase()}.username", project, "dev")
                val pwd = getPriorityProperty("dev.opts.${name.lowercase()}.password", project, "dev")
                credentials {
                    if (StringUtils.hasText(user) && StringUtils.hasText(pwd)) {
                        username = user
                    }
                    if (StringUtils.hasText(pwd)) {
                        password = pwd
                    }
                }
            }
        }
    }

    /**
     * 获取 Maven 配置名称
     */
    abstract fun getMavenName(): String

    /**
     * 获取 Maven 配置 URL
     */
    abstract fun getMavenUrl(project: Project): URI


//    private fun getVersionType(project: Project): String {
//        val matchers = Regex(
//            "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-(" + "(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" + "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$",
//            setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
//        ).find(project.version.toString())
//
//        /* pre-release 部分 */
//        val preRelease: String = matchers?.groups?.get(4)?.value ?: ""
//
//        val versionType = when {
//            preRelease == "" -> "release"
//            Regex("^(M|RC).*", RegexOption.IGNORE_CASE).matches(preRelease) -> "release"
//            Regex("^(alpha|beta|SNAPSHOT).*", RegexOption.IGNORE_CASE).matches(preRelease) -> "snapshot"
//            else -> "snapshot"
//        }
//        return versionType
//    }

    fun getVersionType(project: Project): VersionType {
        return VersionType.forProject(project)
    }
}