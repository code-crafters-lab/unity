pluginManagement {
    includeBuild("../gradle-plugins")
}

plugins {
    id("ccl.repo")
}

include("exception-api")
include("exception-core")

