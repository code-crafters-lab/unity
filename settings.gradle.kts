rootProject.name = "unity"

pluginManagement {
    includeBuild("gradle-plugins")
}

plugins {
    id("com.voc.repo")
}

include("dependencies")

includeBuild(".")
includeBuild("exception")
includeBuild("dict")
includeBuild("response")

