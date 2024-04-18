import io.github.coffee377.gradle.plugin.extensions.AutoIncludeProjectExtension

rootProject.name = "unity"

pluginManagement {
    repositories {
        maven {
            url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public")
            isAllowInsecureProtocol = true
        }
        gradlePluginPortal()
        maven { url = uri("https://repo.spring.io/plugins-release") }
    }

    plugins {
        id("io.github.coffee377.auto-include") version "0.2.0-alpha"
        id("com.google.protobuf") version "0.9.4"
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
//    exclude(".*exception.*")
//    exclude(".*dict-api$")
}