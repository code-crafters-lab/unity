plugins {
    java
    `maven-publish`
}

description = "Unity Build"
group = "org.codecrafterslab"

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
//    apply(plugin = "")
}

configurations.all {
    resolutionStrategy {
//        eachDependency {
//            if (requested.group == "org.projectlombok" && requested.name == "lombok") {
//                println(requested)
//                useVersion("1.18.26")
//            }
//        }
    }
}

