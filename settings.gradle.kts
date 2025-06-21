rootProject.name = "unity"

pluginManagement {
    includeBuild("gradle-plugins")
}

plugins {
    id("ccl.repo")
}

include(":dependencies")

includeBuild(".")
includeBuild("exception")
includeBuild("dict")
//includeBuild("oauth2")

//includeBuild("response")

