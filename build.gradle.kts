plugins {
    id("com.voc.publish")
}

group = "org.codecrafterslab"
description = "Unity Build"

subprojects {
    group = "org.codecrafterslab.unity"
    apply(plugin = "com.voc.publish")
    apply(plugin = "net.jqsoft.nexus3")

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



