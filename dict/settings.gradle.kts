pluginManagement {
    includeBuild("../gradle-plugins")
}

plugins {
    id("ccl.repo")
}

include("dict-api")
include("dict-core")
include("dict-autoconfigure")
include("dict-starter")