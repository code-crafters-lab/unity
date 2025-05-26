plugins {
}

dependencies {
    api(project(":dict-api"))
    api(project(":dict-core"))
    api(project(":dict-autoconfigure"))

    testImplementation("com.grapecitysoft.documents:gcexcel:8.0.6")
}