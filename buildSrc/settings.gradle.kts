/* 插件管理 */
pluginManagement {
    repositories {
        maven { url = uri("D:\\Project\\personal\\gradle-devtools\\build\\publications\\repos") }
        maven {
            url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public/")
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
        maven { url = uri("https://repo.spring.io/plugins-release") }
    }

    /* 插件版本管理 */
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.6.21"
        id("org.gradle.kotlin.kotlin-dsl") version "2.4.1"
    }

}

dependencyResolutionManagement {

}