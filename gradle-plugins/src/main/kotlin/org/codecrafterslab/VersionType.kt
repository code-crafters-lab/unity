package org.codecrafterslab

import org.gradle.api.Project
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull

enum class VersionType(private val type: String, private val description: String) {

    SNAPSHOT("snapshot", "快照版本"),
    ALPHA("alpha", "开发版本"),
    BETA("beta", "测试版本"),
    RELEASE_CANDIDATE("rc", "候选版本"),
    MILESTONE("milestone", "里程碑版本"),
    RELEASE("release", "正式版本");

    companion object {
        /**
         * @see <a href="https://semver.org">Semantic Versioning</a>
         * @since 0.4.0
         */
        val REGEX = Regex(
            "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-(" + "(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" + "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$",
            setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
        )

        fun forProject(@NonNull project: Project): VersionType {
            val matchers = REGEX.find(project.version.toString())

            /* pre-release 部分 */
            val preRelease: String = matchers?.groups?.get(4)?.value ?: ""

            return when {
                preRelease == "" -> RELEASE
                Regex("^(M).*", RegexOption.IGNORE_CASE).matches(preRelease) -> MILESTONE
                Regex("^(RC).*", RegexOption.IGNORE_CASE).matches(preRelease) -> RELEASE_CANDIDATE
                Regex("^(alpha).*", RegexOption.IGNORE_CASE).matches(preRelease) -> ALPHA
                Regex("^(beta).*", RegexOption.IGNORE_CASE).matches(preRelease) -> BETA
                Regex("^(SNAPSHOT).*", RegexOption.IGNORE_CASE).matches(preRelease) -> SNAPSHOT
                else -> SNAPSHOT
            }
        }
    }

    fun isPreRelease(): Boolean = this != RELEASE
}

