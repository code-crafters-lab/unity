pluginManagement {
    repositories {
        mavenLocal()
        maven { url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public"); isAllowInsecureProtocol = true }
        maven {
            url = uri("https://packages.aliyun.com/5f6a9b06d24814603933faab/maven/2038604-snapshot-xnrepo")
            credentials {
                username = "5f4ba059fa82bfeb805a1e09"
                password = "a3XkZLNApybs"
            }
        }

        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://repo.spring.io/plugins-release") }
        gradlePluginPortal()
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
                    useVersion(extra.properties["inner.plugins.version"].toString())
                    if (logger.isDebugEnabled) {
                        logger.warn("plugin {} use version {}", target.id, target.version)
                    }
                }
            }
        }
    }
}


dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven { url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public"); isAllowInsecureProtocol = true }
        maven {
            url = uri("https://packages.aliyun.com/5f6a9b06d24814603933faab/maven/2038604-snapshot-xnrepo")
            credentials {
                username = "5f4ba059fa82bfeb805a1e09"
                password = "a3XkZLNApybs"
            }
        }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
//        maven { url = uri("https://repo.spring.io/plugins-release") }
    }

    // https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/resolve/RepositoriesMode.html
    // repositoriesMode = RepositoriesMode.PREFER_SETTINGS
}

plugins {
//    id("com.voc.repo")
// 使用 settings 插件会报错
//    id("com.voc.settings")
}

