dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        gradlePluginPortal()
    }
    // https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/resolve/RepositoriesMode.html
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
}

pluginManagement {

    repositories {
        maven {
            url = uri(
                File(
                    rootDir.parentFile,
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
    }

    resolutionStrategy {
        eachPlugin {
            innerPluginList.forEach {
                if (requested.id.id.startsWith(it)) {
                    useVersion(extra.properties["inner.version"].toString())
                    if (logger.isDebugEnabled) {
                        logger.warn("plugin {} use version {}", target.id, target.version)
                    }
                }
            }
        }
    }
}

