rootProject.name = "unity"

pluginManagement {
    includeBuild("gradle-plugins")
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


include("dependencies")
include("exception:exception-api")

//includeBuild(".") {
//    dependencySubstitution {
//        substitute(module("org.codecrafterslab.unity:dependencies")).using(project(":dependencies"))
//    }
//}
//
//includeBuild("exception") {
//    dependencySubstitution {
//        substitute(module("org.codecrafterslab.unity:exception-api")).using(project("::exception-api"))
//        substitute(module("org.codecrafterslab.unity:exception-core")).using(project("::exception-core"))
//    }
//}
//
//includeBuild("dict") {
//    dependencySubstitution {
//        substitute(module("org.codecrafterslab.unity:dict-api")).using(project("::dict-api"))
//        substitute(module("org.codecrafterslab.unity:dict-core")).using(project("::dict-core"))
//    }
//}


