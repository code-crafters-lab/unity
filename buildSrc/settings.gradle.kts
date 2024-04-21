/* 插件管理 */
pluginManagement {
    repositories {
        maven {
            url = uri(
                File(
                    settings.rootDir.parent,
                    arrayOf(Project.DEFAULT_BUILD_DIR_NAME, "maven-repository").joinToString("/")
                )
            )
        }
        maven { url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public/"); isAllowInsecureProtocol = true }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://repo.spring.io/plugins-release") }
    }

    /* 插件版本管理 */
    plugins {
        id("com.voc.lib") apply false
    }

    resolutionStrategy {
        eachPlugin {
            listOf("com.voc", "net.jqsoft").forEach {
                if (requested.id.id.startsWith(it)) {
                    useVersion("0.4.0-beta.7")
                    println("${target.id} => ${target.version}")
                }
            }
        }
    }

}

dependencyResolutionManagement {

}
