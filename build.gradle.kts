plugins {
    id("ccl.bom")
}

group = "org.codecrafterslab"
description = "Unity Build"

allprojects {
    group = "org.codecrafterslab.unity"
    apply(plugin = "ccl.publish.aliyun")
}

listOf("build", "clean", "publish", "publishToMavenLocal").forEach { task ->
    tasks.named(task) {
        dependsOn(
            ":dependencies:${task}",
        )
    }
}