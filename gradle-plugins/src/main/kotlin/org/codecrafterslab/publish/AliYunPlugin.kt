package org.codecrafterslab.publish

import org.codecrafterslab.VersionType
import org.gradle.api.Project
import java.net.URI

/**
 * 阿里云效仓库插件
 */
class AliYunPlugin : MavenBasePlugin() {
    override fun apply(project: Project) {
        // todo 插件配置
        project.plugins.apply(PublishPlugin::class.java)
        super.apply(project)
    }

    override fun getMavenName(): String {
        return "AliYun"
    }

    override fun getMavenUrl(project: Project): URI {
        val namespace = "5f6a9b06d24814603933faab"
        var serverId: String
        serverId = "2038604-release-0bMxsA"
        getVersionType(project).let {
            serverId = if (VersionType.RELEASE == it) {
                "2038604-release-0bMxsA"
            } else {
                "2038604-snapshot-XNRePo"
            }
        }
        return project.uri("https://packages.aliyun.com/${namespace}/maven/${serverId}")
    }

    /**
     * @see <a href="https://semver.org">Semantic Versioning</a>
     * @since 0.4.0
     */
    fun getVersionType2(project: Project): String {
        val matchers = VersionType.REGEX.find(project.version.toString())

        /* pre-release 部分 */
        val preRelease: String = matchers?.groups?.get(4)?.value ?: ""

        val versionType = when {
            preRelease == "" -> "release"
            Regex("^(M|RC).*", RegexOption.IGNORE_CASE).matches(preRelease) -> "release"
            Regex("^(alpha|beta|SNAPSHOT).*", RegexOption.IGNORE_CASE).matches(preRelease) -> "snapshot"
            else -> "snapshot"
        }
        return versionType
    }
}