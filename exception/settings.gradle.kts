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
        repositoriesMode = RepositoriesMode.PREFER_SETTINGS
    }
}

include("exception-api")
include("exception-core")

