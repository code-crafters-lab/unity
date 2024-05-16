plugins {
    id("com.voc.bom")
}

description = "Unity Dict"
group = "org.codecrafterslab.unity"

subprojects {
    group = "org.codecrafterslab.unity"
    apply(plugin = "com.voc.publish")
    apply(plugin = "net.jqsoft.nexus3")
}

listOf("build", "clean", "publish").forEach { task ->
    tasks.named(task) {
        dependsOn(
            ":dict-api:${task}",
            ":dict-core:${task}",
            ":dict-autoconfigure:${task}",
            ":dict-starter:${task}"
        )
    }
}