import io.github.coffee377.gradle.plugin.extensions.AutoIncludeProjectExtension

rootProject.name = "unity"

pluginManagement {
    repositories {
        maven {
            url = uri(
                File(
                    settings.rootDir,
                    arrayOf(Project.DEFAULT_BUILD_DIR_NAME, "maven-repository").joinToString("/")
                )
            )
        }
        maven { url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public/"); isAllowInsecureProtocol = true }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://repo.spring.io/plugins-release") }
    }

    val innerPluginList = extra.properties["inner.plugins"].toString()
        .split(",").map { it.trim() }.filter { it != "" }
        .toList()

    plugins {
        // 如果插件已经在类路径上，则插件声明不能使用版本
        innerPluginList.forEach { id(it) apply false }
        id("com.google.protobuf") version "0.9.4" apply false
        id("io.github.coffee377.auto-include") version "0.2.0-alpha" apply false
    }

    resolutionStrategy {
        eachPlugin {
            innerPluginList.forEach {
                if (requested.id.id.startsWith(it)) {
                    useVersion(extra.properties["version"].toString())
                    if (logger.isDebugEnabled) {
                        logger.debug("plugin {} use version {}", target.id, target.version)
                    }
                }
            }
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs {

    }
}

plugins {
    id("io.github.coffee377.auto-include")
}

configure<AutoIncludeProjectExtension> {
    exclude(".*gateway$")
//    exclude(".*dict-api$")
}