pluginManagement {
    includeBuild("../gradle-plugins")
}

plugins {
    id("com.voc.repo")
}

include("exception-api")
include("exception-core")

