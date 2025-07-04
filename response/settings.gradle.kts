pluginManagement {
    includeBuild("../gradle-plugins")
}

plugins {
    id("ccl.repo")
}

include("response-api", "response-core", "response-autoconfigure", "response-starter")
