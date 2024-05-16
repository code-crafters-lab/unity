pluginManagement {
    includeBuild("../gradle-plugins")
}

plugins {
    id("com.voc.repo")
}

include("dict-api")
include("dict-core")
include("dict-autoconfigure")
include("dict-starter")