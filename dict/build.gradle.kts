plugins {
    id("ccl.bom")
}

description = "Unity Dict"
group = "org.codecrafterslab.unity"

allprojects {
    group = "org.codecrafterslab.unity"
    apply(plugin = "ccl.publish.aliyun")

    tasks.withType<JavaCompile> {
        options.release.set(8)
    }

}

subprojects {
    apply(plugin = "ccl.lib")
}

listOf("build", "clean", "publish", "publishToMavenLocal", "publishAllPublicationsToAliYunRepository").forEach { task ->
    tasks.named(task) {
        dependsOn(
            ":dict-api:${task}",
            ":dict-core:${task}",
            ":dict-autoconfigure:${task}",
            ":dict-starter:${task}"
        )
    }
}
