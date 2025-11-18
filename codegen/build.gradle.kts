plugins {
    id("ccl.bom")
}

description = "Unity Code gen"
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

val moduleName  = "codegen"

listOf("build", "clean", "publish").forEach { task ->
    tasks.named(task) {
//        dependsOn(
//            ":${moduleName}-api:${task}",
//            ":${moduleName}-core:${task}",
//            ":${moduleName}-autoconfigure:${task}",
//            ":${moduleName}-starter:${task}"
//        )
    }
}

