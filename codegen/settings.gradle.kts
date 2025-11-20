pluginManagement {
    includeBuild("../gradle-plugins")
}

plugins {
    id("ccl.repo")
}

include(":openapi")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
