package org.codecrafterslab.publish

import org.codecrafterslab.VersionType
import org.codecrafterslab.gradle.utils.PropertyUtils.Companion.getPriorityProperty
import org.gradle.api.Project

/**
 * 阿里云效仓库插件
 */
class AliYunPlugin : MavenBasePlugin() {
    override fun apply(project: Project) {
        project.plugins.apply(PublishPlugin::class.java)
        super.apply(project)
    }

    override fun getMavenName(): String = "AliYun"

    override fun getRepositoryHost(project: Project): String {
        return getPriorityProperty("dev.opts.${getMavenName()}.host", project, "https://packages.aliyun.com").toString()
    }

    override fun getRepositoryNamespace(project: Project): String {
        val namespace = getPriorityProperty(
            "dev.opts.${getMavenName()}.namespace",
            project,
            "5f6a9b06d24814603933faab"
        )

        return "${namespace}/maven"
    }

    override fun getRepositoryId(versionType: VersionType, project: Project): String {
        return if (VersionType.RELEASE == versionType) {
            "2038604-release-0bMxsA"
        } else {
            "2038604-snapshot-XNRePo"
        }
        return super.getRepositoryId(versionType, project)
    }
}
