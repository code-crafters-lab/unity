plugins {
    id("ccl.bom")
}

description = "Unity Exception"
group = "org.codecrafterslab.unity"

allprojects {
    apply(plugin = "ccl.publish.aliyun")

    tasks.withType<JavaCompile> {
        options.release.set(8)
    }
}

subprojects {
    group = "org.codecrafterslab.unity"
    apply(plugin = "ccl.lib")
}

listOf("build", "clean", "publish").forEach { task ->
    tasks.named(task) {
        dependsOn(":exception-api:${task}", ":exception-core:${task}")
    }
}
