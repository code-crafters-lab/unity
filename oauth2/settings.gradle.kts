pluginManagement {
    includeBuild("../gradle-plugins")

    plugins {
        id("org.springframework.boot") version "3.3.4" apply false
        id("io.spring.dependency-management") version "1.1.6" apply false
    }
}

plugins {
    id("ccl.repo")
}

include("oauth2-authorization-server")
include("oauth2-resource-server")
include("oauth2-security")

dependencyResolutionManagement {}