pluginManagement {
    includeBuild("../gradle-plugins")
}

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
        mavenCentral()
    }
    rulesMode = RulesMode.PREFER_PROJECT
}

include("exception-api", "exception-core")

