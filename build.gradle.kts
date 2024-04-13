plugins {
    java
    `maven-publish`
}

description = "Unity Build"
group = "org.codecrafterslab"
version = "1.0.0-SNAPSHOT"

subprojects {
    group = "org.codecrafterslab.unity"
    apply(plugin = "maven-publish")

    repositories {
        mavenLocal()
        maven {
            url = uri("http://nexus.jqk8s.jqsoft.net/repository/maven-public")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        mavenCentral()
    }


    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.MINUTES)
    }
}

allprojects {
    publishing {
        repositories {
            maven {
                name = "dev"
                url = uri("${rootProject.buildDir}/publications/repos")
            }
        }
    }
}

