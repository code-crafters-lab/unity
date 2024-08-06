package net.jqsoft

import org.codecrafterslab.gradle.utils.PropertyUtils.Companion.getPriorityProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

class Nexus3Plugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(MavenPublishPlugin::class.java) {
            val publishing = project.extensions.getByType(PublishingExtension::class.java)
            publishing.repositories.maven {
                name = "Nexus3"
                val nexus3Host = getPriorityProperty("dev.opts.nexus.host", project, "http://localhost:8081")
                url = project.uri("${nexus3Host}/repository/maven-${getVersionType(project)}/")
                isAllowInsecureProtocol = true
                val user = getPriorityProperty("dev.opts.nexus.username", project, "dev")
                val pwd = getPriorityProperty("dev.opts.nexus.password", project, "dev")
                credentials {
                    username = user
                    password = pwd
                }
            }
        }
    }

    /**
     * @see <a href="https://semver.org">Semantic Versioning</a>
     * @since 0.4.0
     */
    private fun getVersionType(project: Project): String {
        val matchers = Regex(
            "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-(" + "(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" + "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$",
            setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
        ).find(project.version.toString())

        /* pre-release 部分 */
        val preRelease: String = matchers?.groups?.get(4)?.value ?: ""

        val versionType = when {
            preRelease == "" -> "releases"
            Regex("^(M|RC|beta).*", RegexOption.IGNORE_CASE).matches(preRelease) -> "pre-releases"
            Regex("^(alpha|SNAPSHOT).*", RegexOption.IGNORE_CASE).matches(preRelease) -> "snapshots"
            else -> "snapshots"
        }
        return versionType
    }
}