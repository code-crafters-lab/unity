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
                isAllowInsecureProtocol = true
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
    protected open fun getMavenUrl(project: Project): URI {
        val host = getRepositoryHost(project)
        val namespace = getRepositoryNamespace(project)
        val plural =
            getPriorityProperty(
                "dev.opts.${getMavenName().lowercase()}.repository.id.plural",
                project,
                "false"
            ).toBoolean()
        var repoId = getRepositoryId(VersionType.forProject(project), project)
        repoId = if (plural) "${repoId}s" else repoId
        return project.uri("${host}/${namespace}/${repoId}")
    }

    protected open fun getRepositoryHost(project: Project): String {
        return getPriorityProperty("dev.opts.${getMavenName()}.host", project, "http://localhost:8081").toString()
    }

    protected open fun getRepositoryNamespace(project: Project): String {
        return getPriorityProperty("dev.opts.${getMavenName()}.namespace", project, "repository").toString()
    }

    protected open fun getRepositoryId(versionType: VersionType, project: Project): String {
        return when (versionType) {
            VersionType.SNAPSHOT -> "snapshot"
            VersionType.ALPHA -> "snapshot"
            VersionType.BETA -> "snapshot"

            VersionType.RELEASE_CANDIDATE -> "release"
            VersionType.MILESTONE -> "release"
            VersionType.RELEASE -> "release"
        }
    }

}